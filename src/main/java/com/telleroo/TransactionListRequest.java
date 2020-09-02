package com.telleroo;

import org.joda.time.LocalDate;

public class TransactionListRequest {
    private String accountId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private int page;

    public String getAccountId() {
        return accountId;
    }

    public TransactionListRequest withAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public TransactionListRequest withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public TransactionListRequest withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public TransactionListRequest withStatus(String status) {
        this.status = status;
        return this;
    }

    public TransactionListRequest withStatus(TransactionState status) {
        this.status = status == null ? null : status.getSearchKey();
        return this;
    }

    public int getPage() {
        return page;
    }

    public TransactionListRequest withPage(int page) {
        this.page = page;
        return this;
    }
}
