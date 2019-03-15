package com.telleroo.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telleroo.Configuration;
import com.telleroo.TellerooError;
import com.telleroo.TellerooException;
import com.telleroo.TellerooServerException;
import com.telleroo.util.ObjectMapperFactory;
import com.telleroo.util.RateLimiter;
import com.telleroo.util.RequestParameterMapper;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClient {
    private static final String HEADER_API_KEY = "Authorization";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final int TIMEOUT_MILLIS = -1;

    private final RequestParameterMapper parameterMapper = new RequestParameterMapper();
    private final ObjectMapper objectMapper = ObjectMapperFactory.make();
    private final Configuration configuration;
    private final CloseableHttpClient httpClient;
    private final ThreadLocal<HttpClientContext> httpContext = new ThreadLocal<>();
    private final RateLimiter rateLimiter;

    public HttpClient(Configuration configuration) {
        this.configuration = configuration;
        this.httpClient = makeHttpClient(configuration);
        this.rateLimiter = new RateLimiter(configuration.getRequestsPerSecond(), configuration.getRequestBurstSize());
    }

    public <T> T get(String path, Object parameters, Class<T> responseType) {
        return executeAndTransform(new HttpGet(getUri(path, parameters)), responseType);
    }

    public <T> T post(String path, Object data, Class<T> responseType) {
        HttpPost request = setPayload(new HttpPost(getUri(path, null)), data);
        return executeAndTransform(request, responseType);
    }

    public <T> T delete(String path, Object parameters, Class<T> responseType) {
        return executeAndTransform(new HttpDelete(getUri(path, parameters)), responseType);
    }

    private <T> T executeAndTransform(HttpUriRequest request, Class<T> responseType) {
        String content = null;
        try {
            content = execute(request);
            return content == null ? null : objectMapper.readValue(content, responseType);
        } catch (IOException e) {
            throw throwError(content, e);
        }
    }

    private <T> T executeAndTransform(HttpUriRequest request, TypeReference<T> responseType) {
        String content = null;
        try {
            content = execute(request);
            return objectMapper.readValue(content, responseType);
        } catch (IOException e) {
            throw throwError(content, e);
        }
    }

    private String execute(HttpUriRequest request) throws IOException {
        if (configuration.isBlockTillRateLimitReset()) {
            rateLimiter.blockTillRateLimitReset();
        }

        request.addHeader(HEADER_API_KEY, configuration.getApiKey());
        request.addHeader(HEADER_USER_AGENT, configuration.getUserAgent());
        request.addHeader("Accepts", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(request, getHttpContext())) {
            if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
                throw throwError(response);
            }

            return readEntity(response);
        }
    }

    private <T extends HttpEntityEnclosingRequest> T setPayload(T request, Object dataObject) {
        try {
            Map<String, String> data = parameterMapper.writeToMap(dataObject);

            List<NameValuePair> form = new ArrayList<>();
            for (Map.Entry<String, String> entry : data.entrySet()) {
//                if (entry.getValue() instanceof String) {
                form.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//                } else {
//                    form.add(new BasicNameValuePair(entry.getKey(), objectMapper.writeValueAsString(entry.getValue())));
//                }
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            request.setEntity(entity);
            return request;
        } catch (Exception e) {
            throw new TellerooException(e);
        }
    }

    private TellerooException throwError(CloseableHttpResponse response) {
        Header contentType = response.getFirstHeader("Content-Type");
        if (contentType != null && contentType.getValue().startsWith("application/json")) {
            String content = null;
            try {
                content = readEntity(response);
                return new TellerooServerException(
                        response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase(),
                        objectMapper.readValue(content, TellerooError.class));
            } catch (IOException ignore) {
                return new TellerooServerException(
                        response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase(),
                        new TellerooError(content));
            }
        } else {
            return new TellerooServerException(
                    response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase(),
                    null);
        }
    }

    private TellerooException throwError(String content, IOException e) {
        try {
            return new TellerooServerException(
                    HttpStatus.SC_OK,
                    "OK",
                    objectMapper.readValue(content, TellerooError.class));
        } catch (IOException ignore) {
            return new TellerooException(e);
        }
    }

    private String readEntity(CloseableHttpResponse response) throws IOException {
        if (response.getEntity() == null) {
            return null;
        }
        return EntityUtils.toString(response.getEntity());
    }


    private CloseableHttpClient makeHttpClient(Configuration configuration) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(configuration.getMaxConnectionsPerRoute());
        connectionManager.setDefaultMaxPerRoute(configuration.getMaxConnectionsPerRoute());
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    private HttpClientContext getHttpContext() {
        HttpClientContext context = httpContext.get();
        if (context == null) {
            context = HttpClientContext.create();
            context.setRequestConfig(RequestConfig.custom()
                    .setSocketTimeout(TIMEOUT_MILLIS)
                    .setConnectTimeout(TIMEOUT_MILLIS)
                    .setConnectionRequestTimeout(TIMEOUT_MILLIS)
                    .build());
            httpContext.set(context);
        }
        return context;
    }

    private URI getUri(String path, Object params) {
        StringBuilder uri = new StringBuilder(configuration.getEndpoint())
                .append("/")
                .append(path);

        if (params != null) {
            uri.append(parameterMapper.write(params));
        }

        try {
            return new URI(uri.toString());
        } catch (URISyntaxException e) {
            throw new TellerooException(e);
        }
    }
}
