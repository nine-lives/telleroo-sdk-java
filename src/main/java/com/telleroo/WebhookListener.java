package com.telleroo;

public interface WebhookListener {
    default void newCredit(Transaction transaction) {
    }

    default void failedPayment(Transaction transaction) {
    }

    default void sentPayment(Transaction transaction) {
    }

    default void companyApproved(Company company) {
    }

    default void error(Exception e, String payload) {

    }
}