package com.telleroo

import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import com.telleroo.util.RequestParameterMapper
import spock.lang.Specification

class RecipientSpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
                {
                  "recipient": {
                    "id": "ff17b231-2bc4-485e-967e-231867e15fd6",
                    "name": "John Archer",
                    "currency_code": "GBP",
                    "account_no": "12345678",
                    "sort_code": "123456",
                    "status": "new",
                    "legal_type": "PRIVATE"
                  }
                }
       '''

        when:
        Recipient entity = mapper.readValue(payload, Recipient.RecipientWrapper).getRecipient()

        then:
        entity.id == 'ff17b231-2bc4-485e-967e-231867e15fd6'
        entity.name == 'John Archer'
        entity.currencyCode == 'GBP'
        entity.accountNo == '12345678'
        entity.sortCode == '123456'
        entity.status == 'new'
        entity.legalType == LegalType.PRIVATE
    }

    def "I can covert a list JSON payload to the entity"() {
        given:
        String payload = '''
                {
                  "recipients": [
                    {
                      "id": "ff17b231-2bc4-485e-967e-231867e15fd6",
                      "name": "John Archer",
                      "currency_code": "GBP",
                      "account_no": "12345678",
                      "sort_code": "123456",
                      "legal_type": "PRIVATE"
                    },
                    {
                      "id": "1fdfef97-95b8-4985-917a-5d9ac9d52d35",
                      "name": "António Silva",
                      "currency_code": "EUR",
                      "iban": "12345678901234567",
                      "bic": "12345678901",
                      "legal_type": "BUSINESS"
                    }
                  ]
                }
       '''

        when:
        RecipientList entity = mapper.readValue(payload, RecipientList)

        then:
        entity.recipients.size() == 2
        entity.recipients[0].id == 'ff17b231-2bc4-485e-967e-231867e15fd6'
        entity.recipients[1].id == '1fdfef97-95b8-4985-917a-5d9ac9d52d35'
        entity.recipients[1].iban == '12345678901234567'
        entity.recipients[1].bic == '12345678901'
        entity.recipients[1].legalType == LegalType.BUSINESS
    }

    def "I can covert a request to a payload with account no"() {
        given:
        Recipient request = Recipient.builder()
                .withCurrencyCode("GBP")
                .withName("John Wick")
                .withLegalType(LegalType.PRIVATE)
                .withAccountNo("12345678")
                .withSortCode("123456")
                .withIban("1234567890")
                .withBic("12345678")
                .build()

        when:
        Map<String, String> entity = new RequestParameterMapper().writeToMap(request)

        then:
        request.name == 'John Wick'
        entity.name == 'John Wick'
        request.accountNo == '12345678'
        entity.account_no == '12345678'
        request.sortCode == '123456'
        entity.sort_code == '123456'
        request.currencyCode == 'GBP'
        entity.currency_code == 'GBP'
        request.legalType == LegalType.PRIVATE
        entity.legal_type == 'PRIVATE'
        request.iban == '1234567890'
        entity.iban == '1234567890'
        request.bic == '12345678'
        entity.bic == '12345678'

        when:
        Recipient result = mapper.readValue(mapper.writeValueAsString(entity), Recipient)

        then:
        result.name == request.name
    }
}