package com.telleroo;

import org.joda.time.DateTime;

public class Transaction {
    private String id;
    private DateTime createdAt;
    private DateTime updatedAt;
    private DateTime processedAt;
    private String transactionId;
    private String transactionType;
    private String currencyCode;
    private int amount;
    private String senderName;
    private String senderAccountNo;
    private String senderSortCode;
    private String recipientId;
    private TransactionState status;
    private String statusInfo;
    private String reconciliation;
    private String reference;
    private String accountId;
    private String tag;
    private int endBalance;
    private String idempotentKey;

    public String getId() {
        return id == null ? transactionId : id;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public DateTime getProcessedAt() {
        return processedAt;
    }

    public String getTransactionId() {
        return transactionId == null ? id : transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public int getAmount() {
        return amount;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderAccountNo() {
        return senderAccountNo;
    }

    public String getSenderSortCode() {
        return senderSortCode;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public TransactionState getStatus() {
        return status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public String getReconciliation() {
        return reconciliation;
    }

    public String getReference() {
        return reference;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getTag() {
        return tag;
    }

    public int getEndBalance() {
        return endBalance;
    }

    public String getIdempotentKey() {
        return idempotentKey;
    }

    static class TransactionEntity {
        private Transaction transaction;

        public Transaction getTransaction() {
            return transaction;
        }
    }
}
