package com.telleroo;

import java.math.BigDecimal;

public class Account {
    private String id;
    private String name;
    private String status;
    private String currencyCode;
    private BigDecimal balance;
    private BigDecimal awaitingFundsBalance;
    private BigDecimal awaitingApprovalBalance;
    private String accountNo;
    private String sortCode;
    private String tag;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getAwaitingFundsBalance() {
        return awaitingFundsBalance;
    }

    public BigDecimal getAwaitingApprovalBalance() {
        return awaitingApprovalBalance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getSortCode() {
        return sortCode;
    }

    public String getTag() {
        return tag;
    }

    static class AccountWrapper {
        private Account account;

        public Account getAccount() {
            return account;
        }
    }
}
