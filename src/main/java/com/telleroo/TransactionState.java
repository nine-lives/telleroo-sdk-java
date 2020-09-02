package com.telleroo;

public enum TransactionState {
    PreparingPayment("Preparing Payment", "preparing_payment"),
    PaymentSent("Payment Sent", ""),
    OnHold("On Hold", "on_hold"),
    WaitingForApproval("Waiting for approval", "waiting_for_approval"),
    WaitingForFunds("Waiting for funds", "waiting_for_funds"),
    LookingIntoIt("Looking into it", "looking_into_it"),
    Credit("Credit", "credited"),
    Failed("Failed", "failed"),
    Cancelled("Cancelled", "cancelled");

    private final String transmissionValue;
    private final String searchKey;

    TransactionState(String transmissionValue, String searchKey) {
        this.transmissionValue = transmissionValue;
        this.searchKey = searchKey;
    }

    public String getTransmissionValue() {
        return transmissionValue;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public static TransactionState fromTransmissionValue(String transmissionValue) {
        for (TransactionState transactionState : values()) {
            if (transactionState.getTransmissionValue().equals(transmissionValue)) {
                return transactionState;
            }
        }

        String normalised = normalise(transmissionValue == null ? "" : transmissionValue);
        for (TransactionState transactionState : values()) {
            if (normalise(transactionState.getTransmissionValue()).equals(normalised)) {
                return transactionState;
            }
        }

        throw new IllegalArgumentException("Illegal transaction value fot TransitionState - " + transmissionValue);
    }

    private static String normalise(String value) {
        return value.replaceAll(" ", "").toLowerCase();
    }
}
