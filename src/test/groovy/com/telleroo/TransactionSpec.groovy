package com.telleroo

import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import org.joda.time.DateTime
import spock.lang.Specification

class TransactionSpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
                {
                  "transaction": {
                    "id": "842963c5-e230-42ef-8de8-2b7a459026",
                    "processed_at": "2016-12-01T12:15:23.486Z",
                    "transaction_type": "Debit",
                    "currency_code": "GBP",
                    "amount": 100,
                    "recipient_id": "442663c5-e230-32ef-8de8-1b7a459026",
                    "status": "Preparing Payment",
                    "status_info": "Creating payment request",
                    "reconciliation": "f9q3408rh3",
                    "reference": "Withdrawal Telleroo",
                    "account_id": "ed5af7d2-741c-4905-a3ba-66d332d604",
                    "tag": "Manutd",
                    "end_balance": 2200,
                    "idempotent_key": "6130348",
                    "created_at": "2016-12-02T12:15:22.486Z",
                    "updated_at": "2016-12-02T12:15:22.486Z"
                  }
                }
       '''

        when:
        Transaction entity = mapper.readValue(payload, Transaction.TransactionEntity).getTransaction()

        then:
        entity.id == '842963c5-e230-42ef-8de8-2b7a459026'
        entity.processedAt == DateTime.parse('2016-12-01T12:15:23.486Z')
        entity.transactionType == 'Debit'
        entity.currencyCode == 'GBP'
        entity.amount == 100
        entity.recipientId == '442663c5-e230-32ef-8de8-1b7a459026'
        entity.status == 'Preparing Payment'
        entity.statusInfo == 'Creating payment request'
        entity.reconciliation == 'f9q3408rh3'
        entity.reference == 'Withdrawal Telleroo'
        entity.accountId == 'ed5af7d2-741c-4905-a3ba-66d332d604'
        entity.tag == 'Manutd'
        entity.endBalance == 2200
        entity.idempotentKey == '6130348'
        entity.createdAt == DateTime.parse('2016-12-02T12:15:22.486Z')
        entity.updatedAt == DateTime.parse('2016-12-02T12:15:22.486Z')
    }

    def "I can covert a list JSON payload to the entity"() {
        given:
        String payload = '''
                {
                  "transactions": [
                    {
                      "id": "842963c5-e230-42ef-8de8-2b7a459025",
                      "processed_at": "2016-12-01T12:15:23.486Z",
                      "transaction_type": "Debit",
                      "currency_code": "GBP",
                      "amount": "1000",
                      "sender_name": "Daryl Oates",
                      "status": "Preparing Payment",
                      "status_info": "Creating payment request",
                      "reconciliation": "3456yujk",
                      "reference": "Funding Telleroo",
                      "account_id": "ed5af7d2-741c-4905-a3ba-66d332d604",
                      "tag": "Sponsoring",
                      "end_balance": 1560,
                      "created_at": "2016-12-01T12:15:22.486Z",
                      "updated_at": "2016-12-01T12:15:22.486Z"
                    },
                    {
                      "id": "842963c5-e230-42ef-8de8-2b7a459026",
                      "processed_at": "2016-12-01T12:15:23.486Z",
                      "transaction_type": "Debit",
                      "currency_code": "GBP",
                      "amount": "100",
                      "recipient_id": "122963c5-e230-42ef-8de8-327459026",
                      "status": "Preparing Payment",
                      "status_info": "Creating payment request",
                      "reconciliation": "f9q3408rh3",
                      "reference": "Withdrawal Telleroo",
                      "account_id": "ed5af7d2-741c-4905-a3ba-66d332d604",
                      "tag": "Manutd",
                      "end_balance": 1460,
                      "idempotent_key": "6130348",
                      "created_at": "2016-12-01T12:15:22.486Z",
                      "updated_at": "2016-12-01T12:15:22.486Z"
                    }
                  ]
                }
       '''

        when:
        TransactionList entity = mapper.readValue(payload, TransactionList)

        then:
        entity.transactions.size() == 2
        entity.transactions[0].id == '842963c5-e230-42ef-8de8-2b7a459025'
        entity.transactions[0].senderName == 'Daryl Oates'
        entity.transactions[1].id == '842963c5-e230-42ef-8de8-2b7a459026'
    }

}