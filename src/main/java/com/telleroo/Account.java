package com.telleroo;

public class Account {
    private String id;
    private String name;
    private String status;
    private String currencyCode;
    private int balance;
    private int awaitingFundsBalance;
    private int awaitingApprovalBalance;
    private String bankAccount;
    private String accountNo;
    private String sortCode;
    private String tag;
    private boolean xeroFeed;

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

    public int getBalance() {
        return balance;
    }

    public int getAwaitingFundsBalance() {
        return awaitingFundsBalance;
    }

    public int getAwaitingApprovalBalance() {
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

    public String getBankAccount() {
        return bankAccount;
    }

    public boolean hasXeroFeed() {
        return xeroFeed;
    }

    static class AccountWrapper {
        private Account account;

        public Account getAccount() {
            return account;
        }
    }
}
