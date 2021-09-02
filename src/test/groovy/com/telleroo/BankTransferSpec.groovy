package com.telleroo

import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import com.telleroo.util.RequestParameterMapper
import spock.lang.Specification

class BankTransferSpec extends Specification {
    private RequestParameterMapper mapper = new RequestParameterMapper()

    def "I can covert a request to a payload with bank details"() {
        given:
        BankTransfer request = BankTransfer.withBankDetails("John Wick", "12345678", "123456", LegalType.PRIVATE)
                .withCurrencyCode("GBP")
                .withAccountId("account-id")
                .withAmount(1234)
                .withIdempotentKey("ikey")
                .withReconciliation("recon-recon")
                .withReference("ref-ref")
                .withTag("unit-test")
                .build()

        when:
        Map<String, String> entity = mapper.writeToMap(request)

        then:
        request.recipientName == 'John Wick'
        entity.recipient_name == 'John Wick'
        request.accountNo == '12345678'
        entity.account_no == '12345678'
        request.sortCode == '123456'
        entity.sort_code == '123456'
        request.currencyCode == 'GBP'
        entity.currency_code == 'GBP'
        request.accountId == 'account-id'
        entity.account_id == 'account-id'
        request.idempotentKey == 'ikey';
        entity.idempotent_key == 'ikey'
        request.reconciliation == 'recon-recon'
        entity.reconciliation == 'recon-recon'
        request.reference == 'ref-ref'
        entity.reference == 'ref-ref'
        request.tag == 'unit-test'
        entity.tag == 'unit-test'
        request.legalType == LegalType.PRIVATE
        entity.legal_type == 'PRIVATE'

        when:
        ObjectMapper om = ObjectMapperFactory.make()
        BankTransfer result = om.readValue(om.writeValueAsString(entity), BankTransfer)

        then:
        result.idempotentKey == request.idempotentKey
    }

    def "I can covert a request to a payload with recipient id"() {
        given:
        BankTransfer request = BankTransfer.withRecipientId("recipient-id")
                .withCurrencyCode("GBP")
                .withAccountId("account-id")
                .withAmount(1234)
                .withIdempotentKey("ikey")
                .withReconciliation("recon-recon")
                .withReference("ref-ref")
                .withTag("unit-test")
                .build()

        when:
        Map<String, String> entity = mapper.writeToMap(request)

        then:
        entity.recipient_id == 'recipient-id'
        entity.currency_code == 'GBP'
        entity.account_id == 'account-id'
        entity.idempotent_key == 'ikey'
        entity.reconciliation == 'recon-recon'
        entity.reference == 'ref-ref'
        entity.tag == 'unit-test'
    }
}