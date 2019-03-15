package com.telleroo;

public class Recipient {
    private String id;
    private String status;
    private String name;
    private String currencyCode;
    private String accountNo;
    private String sortCode;
    private String iban;
    private String bic;
    private LegalType legalType;

    private Recipient() {
    }

    public Recipient(Builder builder) {
        name = builder.name;
        currencyCode = builder.currencyCode;
        accountNo = builder.accountNo;
        sortCode = builder.sortCode;
        iban = builder.iban;
        bic = builder.bic;
        legalType = builder.legalType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getCurrencyCode() {
        return currencyCode;
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

    public static final class Builder {
        private String name;
        private String currencyCode;
        private String accountNo;
        private String sortCode;
        private String iban;
        private String bic;
        private LegalType legalType;

        private Builder() {

        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder withAccountNo(String accountNo) {
            this.accountNo = accountNo;
            return this;
        }

        public Builder withSortCode(String sortCode) {
            this.sortCode = sortCode;
            return this;
        }

        public Builder withIban(String iban) {
            this.iban = iban;
            return this;
        }

        public Builder withBic(String bic) {
            this.bic = bic;
            return this;
        }

        public Builder withLegalType(LegalType legalType) {
            this.legalType = legalType;
            return this;
        }

        public Recipient build() {
            return new Recipient(this);
        }
    }

    static class RecipientWrapper {
        private Recipient recipient;

        public Recipient getRecipient() {
            return recipient;
        }
    }
}
