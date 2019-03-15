package com.telleroo;

import com.telleroo.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Telleroo SDK entry point
 */
public final class Telleroo {
    private final HttpClient client;

    private Telleroo(Configuration configuration) {
        this.client = new HttpClient(configuration);
    }

    /**
     * Get a Telleroo instance for your given api key.
     *
     * @param apiKey your client id
     * @return a Telleroo instance
     */
    public static Telleroo make(String apiKey) {
        return new Telleroo(new Configuration()
                .withApiKey(apiKey));
    }

    /**
     * Get a Telleroo instance using finer grained control over configuration.
     *
     * @param configuration your configuration
     * @return a Telleroo instance
     */
    public static Telleroo make(Configuration configuration) {
        return new Telleroo(configuration);
    }

    public AccountsList getAccounts() {
        return client.get("accounts", null, AccountsList.class);
    }

    public RecipientList getRecipients(int page) {
        Map<String, Object> request = new HashMap<>();
        request.put("page", page);
        return client.get("recipients", request, RecipientList.class);
    }

    public Recipient getRecipient(String id) {
        return client.get("recipients/" + id, null, Recipient.RecipientWrapper.class).getRecipient();
    }

    public Recipient createRecipient(Recipient recipient) {
        return client.post("recipients", recipient, Recipient.RecipientWrapper.class).getRecipient();
    }

    public void deleteRecipient(String id) {
        client.delete("recipients/" + id, null, Recipient.RecipientWrapper.class);
    }

    public TransactionList getTransactions(TransactionListRequest request) {
        return client.get("transactions", request, TransactionList.class);
    }

    public Transaction getTransaction(String id) {
        return client.get("transactions/" + id, null, Transaction.TransactionEntity.class).getTransaction();
    }

    public Transaction transfer(BankTransfer transfer) {
        if (transfer.getRecipientId() == null) {
            return client.post("adhoc_bank_transfers", transfer, Transaction.TransactionEntity.class).getTransaction();
        } else {
            return client.post("bank_transfers", transfer, Transaction.TransactionEntity.class).getTransaction();
        }
    }

    public void cancelTransfer(String id) {
        client.delete("bank_transfers/" + id, null, Transaction.TransactionEntity.class);
    }

    public UserList getUsers() {
        return client.get("users", null, UserList.class);
    }

    public User getUser(String id) {
        return client.get("users/" + id, null, User.UserWrapper.class).getUser();
    }

    public User createUser(UserCreateRequest request) {
        return client.post("users", request, User.UserWrapper.class).getUser();
    }

    public void deleteUser(String id) {
        client.delete("users/" + id, null, User.UserWrapper.class);
    }
}
