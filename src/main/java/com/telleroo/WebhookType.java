package com.telleroo;

public enum WebhookType {
    NewCredit("New Credit"),
    FailedPayment("Failed Payment"),
    SentPayment("Sent Payment"),
    CompanyApproved("Company Approved");

    private final String value;

    WebhookType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WebhookType getType(String value) {
        for (WebhookType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return valueOf(value);
    }
}
