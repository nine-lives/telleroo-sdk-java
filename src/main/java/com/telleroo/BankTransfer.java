package com.telleroo;

import java.math.BigDecimal;

public final class BankTransfer {
    private String accountId;
    private BigDecimal amount;
    private String currencyCode;
    private String reference;
    private String tag;
    private String reconciliation;
    private String idempotentKey;

    private String recipientName;
    private String accountNo;
    private String sortCode;
    private LegalType legalType;

    private String recipientId;

    private BankTransfer() {

    }

    private BankTransfer(Builder builder) {
        accountId = builder.accountId;
        amount = builder.amount;
        currencyCode = builder.currencyCode;
        reference = builder.reference;
        tag = builder.tag;
        reconciliation = builder.reconciliation;
        idempotentKey = builder.idempotentKey;
        recipientName = builder.recipientName;
        accountNo = builder.accountNo;
        sortCode = builder.sortCode;
        legalType = builder.legalType;
        recipientId = builder.recipientId;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getReference() {
        return reference;
    }

    public String getTag() {
        return tag;
    }

    public String getReconciliation() {
        return reconciliation;
    }

    public String getIdempotentKey() {
        return idempotentKey;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getSortCode() {
        return sortCode;
    }

    public LegalType getLegalType() {
        return legalType;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public static Builder withBankDetails(String recipientName, String accountNo, String sortCode, LegalType legalType) {
        return new Builder(recipientName, accountNo, sortCode, legalType);
    }

    public static Builder withRecipientId(String recipientId) {
        return new Builder(recipientId);
    }

    public static final class Builder {
        private String accountId;
        private BigDecimal amount;
        private String currencyCode;
        private String reference;
        private String tag;
        private String reconciliation;
        private String idempotentKey;

        private String recipientName;
        private String accountNo;
        private String sortCode;
        private LegalType legalType;

        private String recipientId;

        private Builder(String recipientName, String accountNo, String sortCode, LegalType legalType) {
            this.recipientName = recipientName;
            this.accountNo = accountNo;
            this.sortCode = sortCode;
            this.legalType = legalType;
        }

        private Builder(String recipientId) {
            this.recipientId = recipientId;
        }

        public Builder withAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder withCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder withTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder withReconciliation(String reconciliation) {
            this.reconciliation = reconciliation;
            return this;
        }

        public Builder withIdempotentKey(String idempotentKey) {
            this.idempotentKey = idempotentKey;
            return this;
        }

        public BankTransfer build() {
            return new BankTransfer(this);
        }
    }

}
