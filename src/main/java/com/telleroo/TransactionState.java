package com.telleroo;

public enum TransactionState {
    PreparingPayment("Preparing Payment"),
    PaymentSent("Payment Sent"),
    WaitingForFunds("Waiting for funds"),
    LookingIntoIt("Looking into it"),
    Failed("Failed");

    private final String transmissionValue;

    TransactionState(String transmissionValue) {
        this.transmissionValue = transmissionValue;
    }

    public String getTransmissionValue() {
        return transmissionValue;
    }

    public static TransactionState fromTransmissionValue(String transmissionValue) {
        for (TransactionState transactionState : values()) {
            if (transactionState.getTransmissionValue().equals(transmissionValue)) {
                return transactionState;
            }
        }

        throw new IllegalArgumentException("Illegal transaction value fot TransitionState - " + transmissionValue);
    }
}
