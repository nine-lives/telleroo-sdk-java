package com.telleroo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telleroo.util.ObjectMapperFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebhookCallProcessor {
    private static final int DEFAULT_THREADS = 5;
    private final ObjectMapper mapper;
    private final ExecutorService executor;
    private final List<WebhookListener> listeners = new ArrayList<>();

    public WebhookCallProcessor() {
        this(DEFAULT_THREADS);
    }

    public WebhookCallProcessor(int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        ObjectMapperFactory.setFailOnUnknownProperties(true);
        this.mapper = ObjectMapperFactory.make();
    }

    public void addListener(WebhookListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WebhookListener listener) {
        listeners.remove(listener);
    }

    public Future process(String payload) {
        return executor.submit(() -> {
            try {
                WebhookType type = findEntityType(payload);
                switch (type) {
                    case NewCredit:
                    case FailedPayment:
                    case SentPayment:
                        Transaction transaction = readEntity(payload, new TypeReference<Webhook<Transaction>>() { });
                        fire(type, transaction);
                        break;
                    case CompanyApproved:
                        Company company = readEntity(payload, new TypeReference<Webhook<Company>>() { });
                        fire(company);
                        break;
                }
            } catch (Exception e) {
                fire(e, payload);
            }
        });
    }

    private void fire(Exception e, String payload) {
        for (WebhookListener listener : listeners) {
            try {
                listener.error(e, payload);
            } catch (Exception ignore) {
            }
        }
    }

    private void fire(WebhookType type, Transaction transaction) {
        for (WebhookListener listener : listeners) {
            switch (type) {
                case NewCredit:
                    listener.newCredit(transaction);
                    break;
                case FailedPayment:
                    listener.failedPayment(transaction);
                    break;
                case SentPayment:
                    listener.sentPayment(transaction);
                    break;
            }
        }
    }

    private void fire(Company company) {
        for (WebhookListener listener : listeners) {
            listener.companyApproved(company);
        }
    }

    private WebhookType findEntityType(String payload) throws IOException {
        Webhook<Map<String, Object>> webhook = mapper.readValue(payload, new TypeReference<Webhook<Map<String, Object>>>() { });
        return WebhookType.getType(String.valueOf(webhook.getWebhook().get("type")));
    }

    private <T> T readEntity(String payload, TypeReference<Webhook<T>> reference) throws IOException {
        Webhook<Map<String, Object>> raw = mapper.readValue(payload, new TypeReference<Webhook<Map<String, Object>>>() { });
        raw.getWebhook().remove("type");

        Webhook<T> webhook = mapper.readValue(mapper.writeValueAsString(raw), reference);
        return webhook.getWebhook();
    }
}
