# Telleroo Client Java SDK

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.9ls/telleroo-java-sdk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.9ls/telleroo-java-sdk)
[![Build Status](https://api.travis-ci.org/nine-lives/telleroo-sdk-java.png)](https://travis-ci.org/nine-lives/telleroo-sdk-java)
[![Code Quality](https://api.codacy.com/project/badge/grade/d289b210b4b94dc69384622a5732bb05)](https://www.codacy.com/app/nine-lives/telleroo-sdk-java)
[![Coverage](https://api.codacy.com/project/badge/coverage/d289b210b4b94dc69384622a5732bb05)](https://www.codacy.com/app/nine-lives/telleroo-sdk-java)

## Getting Started

The Telleroo API requires you to have an api key/token. 

All API calls are routed from the `Telleroo` object.

```
    Telleroo telleroo = Telleroo.make(apiKey);
```

The sdk is hosted on maven central so you can include it as a dependency 
in your projects as follows:

### Gradle/Grails
```
    compile 'com.9ls:telleroo-java-sdk:1.0.1'
```

### Apache Maven
```
    <dependency>
        <groupId>com.9ls</groupId>
        <artifactId>telleroo-java-sdk</artifactId>
        <version>1.0.1</version>
    </dependency>
```

### Apache Ivy
```
    <dependency org="com.9ls" name="telleroo-java-sdk" rev="1.0.1" />
```

## Create a Recipient

To create a recipient:

```
        Recipient recipient = telleroo.createRecipient(Recipient.builder()
            .withName("John Doe")
            .withAccountNo("12345678")
            .withSortCode("123456")
            .withCurrencyCode("GBP")
            .withLegalType(LegalType.PRIVATE)
            .build())
```

## Execute a Bank Transfer

To create a recipient:

```
        Transaction transaction = telleroo.transfer(BankTransfer
            .withBankDetails("Integeration Spec", "12345678", "123456", LegalType.PRIVATE)
            .withCurrencyCode("GBP")
            .withAccountId(accountId)
            .withAmount(rnd.nextInt(8999))
            .withIdempotentKey(UUID.randomUUID().toString())
            .withReconciliation("recon-" + rnd.nextInt(1000))
            .withReference("ref-" + rnd.nextInt(1000))
            .withTag("integration-test")
```

## Webhooks

To parse a webhook payload you can use the WebhookCallProcessor and register a WebhookListener. 
The WebhookCallProcessor.process call forks calls to the listeners into a separate thread so it will return
immediately. All registered threads are run in a single thread so they will be called serially. The default 
number of threads for WebhookCallProcessor is 5.

You may implement as many or as few of the methods on the WebhookListener interface as you require. Any errors
that occur during either parsing the payload or in a listener will fire the error method on the listener.  

### The WebhookListener

```
    WebhookListener listener = new WebhookListener() {
        public void newCredit(Transaction transaction) { }
        public void failedPayment(Transaction transaction) { }
        public void sentPayment(Transaction transaction) { }
        public void companyApproved(Company company) { }
        public void error(Exception e, String payload) { }
    });
```

### An HttpServlet example
```
    public class TellerooWebhookServlet extends HttpServlet {
        private static final WEBHOOK_TOKEN = "YOUR-WEBHOOK-TOKEN-HERE";
        private WebhookCallProcessor processor = new WebhookCallProcessor()
        private WebhookListener listener = ...

        public void init() throws ServletException {
            processor.addListener(listener);
        }
    
        protected void doPost(HttpServletRequest request, HttpServletResponse response) {
            if (!WEBHOOK_TOKEN.equals(request.getHeader("Authenticity-Token"))) {
               response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                String payload = IOUtils.toString(request.getReader());
                processor.process(payload);
            }
        }
    }
``` 

### A Spring Controller example
```
    @RequestMapping(value = "telleroo/*")
    @Controller
    public class TellerooWebhookServlet extends HttpServlet {
        private static final WEBHOOK_TOKEN = "YOUR-WEBHOOK-TOKEN-HERE";
        private WebhookCallProcessor processor = new WebhookCallProcessor();
        private WebhookListener listener = ...;

        @PostConstruct
        public void init() {
            processor.addListener(listener);
        }
    
        @ResponseBody
        @RequestMapping(value = "/webhook", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "text/plain")
        public String telleroo(HttpEntity<String> httpEntity, HttpServletResponse)  {
            if (!WEBHOOK_TOKEN.equals(httpEntity.getHeaders().get("Authenticity-Token"))) {
               response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                processor.process(httpEntity.getBody());
            }
            return "";
        }
    }
``` 

## Custom Configuration

You can also use `ClientConfiguration` to configure the SDK. Apart
from the the api key all the other values have defaults.

```
    Telleroo telleroo = Telleroo.make(new Configuration()
        .withApiKey(apiKey)
        .withEndpoint("https://api.telleroo.com")
        .withMaxConnectionsPerRoute(20)
        .withUserAgent("telleroo-sdk-java 1.0.1")
        .withBlockTillRateLimitReset(false)
        .withRequestsPerSecond(5)
        .withRequestBurstSize(20);
```

| Configuration Attribute | Description |
| ----------------------- | ----------- |
| Endpoint | The base api url. Defaults to https://api.telleroo.com |
| MaxConnectionsPerRoute | The effective maximum number of concurrent connections in the pool. Connections try to make use of the keep-alive directive. Defaults to 20
| UserAgent | The user agent string sent in the request
| BlockTillRateLimitReset | If set to true then the client will block if the rate limit has been reached until the reset timestamp has expired. Defaults to false
| RequestsPerSecond | If rate limited is true then the maximum requests per second 
| RequestBurstSize | If rate limited the number of consecutive requests allowed before rate limit is enforced 


## Build

Once you have checked out the project you can build and test the project with the following command:

```
    gradlew check -x integrationTest -x jacocoTestReport
```

 