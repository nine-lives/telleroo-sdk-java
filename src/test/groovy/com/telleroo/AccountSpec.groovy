package com.telleroo

import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import spock.lang.Specification

class AccountSpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
            {
              "account":
                {
                  "id": "7e583a6d-0869-4c62-9f2c-74a75b8c2a75",
                  "name": "client account",
                  "currency_code": "GBP",
                  "balance": 500,
                  "awaiting_funds_balance": 640,
                  "awaiting_approval_balance": 100,
                  "account_no": "12345678",
                  "sort_code": "123456",
                  "tag": "UK Marketplace"
                }
            }
       '''

        when:
        Account entity = mapper.readValue(payload, Account.AccountWrapper).getAccount()

        then:
        entity.id == '7e583a6d-0869-4c62-9f2c-74a75b8c2a75'
        entity.name == 'client account'
        entity.currencyCode == 'GBP'
        entity.balance == 500
        entity.awaitingFundsBalance == 640
        entity.awaitingApprovalBalance == 100
        entity.accountNo == '12345678'
        entity.sortCode == '123456'
        entity.tag == 'UK Marketplace'
    }

    def "I can covert a list JSON payload to the entity"() {
        given:
        String payload = '''
                {
                  "accounts": [
                    {
                      "id": "7e583a6d-0869-4c62-9f2c-74a75b8c2a75",
                      "name": "client account",
                      "currency": "GBP",
                      "balance": 500,
                      "awaiting_funds_balance": 640,
                      "awaiting_approval_balance": 100,
                      "account_no": "12345678",
                      "sort_code": "123456",
                      "tag": "UK Marketplace"
                    },
                    {
                      "id": "7e583a6d-0869-4c62-9f2c-74a75b8c2a76",
                      "name": "client account",
                      "currency": "GBP",
                      "balance": 500,
                      "awaiting_funds_balance": 640,
                      "awaiting_approval_balance": 100,
                      "account_no": "12345678",
                      "sort_code": "123456",
                      "tag": "UK Marketplace"
                    }
                  ]
                }
       '''

        when:
        AccountsList entity = mapper.readValue(payload, AccountsList)

        then:
        entity.accounts.size() == 2
        entity.accounts[0].id == '7e583a6d-0869-4c62-9f2c-74a75b8c2a75'
        entity.accounts[1].id == '7e583a6d-0869-4c62-9f2c-74a75b8c2a76'
    }

}