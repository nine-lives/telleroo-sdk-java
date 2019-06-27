package com.telleroo

import org.joda.time.LocalDate

class BankTransferIntegrationSpec extends BaseIntegrationSpec {

    def "I can create an ad-hoc transfer"() {
        given:
        Account account = telleroo.accounts.accounts[0]
        BankTransfer.Builder builder = create(account.id)
        BankTransfer request = builder.build()

        when:
        Transaction response = telleroo.transfer(request)

        then:
        response.accountId == account.id
        response.reference == request.reference
        response.reconciliation == request.reconciliation
        response.currencyCode == request.currencyCode
        response.amount == request.amount
        response.idempotentKey == request.idempotentKey
        response.tag == request.tag
        response.transactionType == 'Debit'

        when:
        Transaction transaction = telleroo.getTransaction(response.id)

        then:
        transaction.accountId == account.id
        transaction.reference == request.reference
        transaction.reconciliation == request.reconciliation
        transaction.currencyCode == request.currencyCode
        transaction.amount == request.amount
        transaction.idempotentKey == request.idempotentKey
        transaction.tag == request.tag
    }


    def "I can create a transfer with recipient id"() {
        given:
        Account account = telleroo.accounts.accounts[0]
        Recipient recipient = telleroo.createRecipient(RecipientIntegrationSpec.create())
        BankTransfer.Builder builder = create(account.id, recipient.id)
        BankTransfer request = builder.build()

        when:
        Transaction response = telleroo.transfer(request)

        then:
        response.accountId == account.id
        response.reference == request.reference
        response.reconciliation == request.reconciliation
        response.currencyCode == request.currencyCode
        response.amount == request.amount
        response.idempotentKey == request.idempotentKey
        response.tag == request.tag
        response.transactionType == 'Debit'
        response.status != null

        when:
        Transaction transaction = telleroo.getTransaction(response.id)

        then:
        transaction.accountId == account.id
        transaction.reference == request.reference
        transaction.reconciliation == request.reconciliation
        transaction.currencyCode == request.currencyCode
        transaction.amount == request.amount
        transaction.idempotentKey == request.idempotentKey
        transaction.tag == request.tag
        transaction.status != null

        cleanup:
        if (recipient) {
            telleroo.deleteRecipient(recipient.id)
        }
    }

    def "I can create an ad-hoc transfer to the same recipient"() {
        given:
        Account account = telleroo.accounts.accounts[0]
        BankTransfer.Builder builder = create(account.id)
        BankTransfer request = builder.build()
        Transaction response1 = telleroo.transfer(request)

        when:
        Transaction response2 = telleroo.transfer(builder.withIdempotentKey(response1.idempotentKey + "x").build())

        then:
        response1.id != response2.id
        response1.recipientId == response2.recipientId
        response2.idempotentKey == response1.idempotentKey + "x"
    }

    def "I can list transactions"() {
        given:
        Account account = telleroo.accounts.accounts[0]
        List<Transaction> transactions = (0..2).collect {
            telleroo.transfer(create(account.id).build())
        }

        when:
        List<Transaction> list = []
        int i = 1
        while(true) {
            TransactionList response = telleroo.getTransactions(new TransactionListRequest()
                    .withAccountId(account.id)
                    .withStartDate(LocalDate.now())
                    .withEndDate(LocalDate.now())
                    .withPage(i++))

            if (response.transactions.isEmpty()) {
                break
            }

            list.addAll(response.transactions)
        }


        then:
        list.find { it && it?.id == transactions[0].id }
        list.find { it && it?.id == transactions[1].id }
        list.find { it && it?.id == transactions[2].id }
    }

    def "I can a 422 when I try to fetch an invalid id"() {
        when:
        telleroo.getRecipient("invalid-id")

        then:
        TellerooServerException e = thrown(TellerooServerException)
        e.statusCode == 422
        e.error.message == "Recipient does not exist"
    }

    def "I get an error if I try to create a recipient with incomplete details"() {
        when:
        Recipient response = telleroo.createRecipient(Recipient.builder()
                .withName("Bob")
                .withLegalType(LegalType.PRIVATE)
                .build())

        then:
        TellerooServerException e = thrown(TellerooServerException)
        e.statusCode == 422
        e.error.message == "Invalid request, check field errors"
        e.error.fieldErrors.size() == 2
        e.error.fieldErrors.currency_code.size() == 2
        e.error.fieldErrors.currency_code[0] == "can't be blank"
        e.error.fieldErrors.currency_code[1] == "is not included in the list"
        e.error.fieldErrors.name.size() == 1

        cleanup:
        if (response) {
            telleroo.deleteRecipient(response.id)
        }
    }

    static BankTransfer.Builder create(String accountId) {
        Random rnd = new Random()
        String accountNo = String.valueOf(rnd.nextInt(89999999) + 10000000)
        BankTransfer.withBankDetails("Integeration Spec", accountNo, "123456", rnd.nextInt() % 2 == 0? LegalType.PRIVATE : LegalType.BUSINESS)
            .withCurrencyCode("GBP")
            .withAccountId(accountId)
            .withAmount(rnd.nextInt(8999))
            .withIdempotentKey(UUID.randomUUID().toString())
            .withReconciliation("recon-" + rnd.nextInt(1000))
            .withReference("ref-" + rnd.nextInt(1000))
            .withTag("integration-test")
    }

    static BankTransfer.Builder create(String accountId, String recipientId) {
        Random rnd = new Random()
        BankTransfer.withRecipientId(recipientId)
                .withCurrencyCode("GBP")
                .withAccountId(accountId)
                .withAmount(rnd.nextInt(8999))
                .withIdempotentKey(UUID.randomUUID().toString())
                .withReconciliation("recon-" + rnd.nextInt(1000))
                .withReference("ref-" + rnd.nextInt(1000))
                .withTag("integration-test")
    }
}
