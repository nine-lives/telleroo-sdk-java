package com.telleroo

import com.telleroo.util.RequestParameterMapper
import org.joda.time.LocalDate
import spock.lang.Specification

class TransactionListRequestSpec extends Specification {
    private RequestParameterMapper mapper = new RequestParameterMapper()

    def "I can covert a request to a payload"() {
        given:
        TransactionListRequest request = new TransactionListRequest()
                .withAccountId("account-id")
                .withStartDate(LocalDate.parse("2019-01-02"))
                .withEndDate(LocalDate.parse("2019-02-03"))
                .withStatus("pending")
                .withPage(1)
        when:
        Map<String, String> entity = mapper.writeToMap(request)

        then:
        entity.account_id == 'account-id'
        entity.start_date == '2019-01-02'
        entity.end_date == '2019-02-03'
        entity.status == 'pending'
        entity.page == '1'
        request.accountId == 'account-id'
        request.startDate == LocalDate.parse('2019-01-02')
        request.endDate == LocalDate.parse('2019-02-03')
        request.status == 'pending'
        request.page == 1
    }

    def "I can covert a request to a payload using state enum"() {
        given:
        TransactionListRequest request = new TransactionListRequest()
                .withAccountId("account-id")
                .withStartDate(LocalDate.parse("2019-01-02"))
                .withEndDate(LocalDate.parse("2019-02-03"))
                .withStatus(TransactionState.LookingIntoIt)
                .withPage(1)
        when:
        Map<String, String> entity = mapper.writeToMap(request)

        then:
        entity.account_id == 'account-id'
        entity.start_date == '2019-01-02'
        entity.end_date == '2019-02-03'
        entity.status == TransactionState.LookingIntoIt.searchKey
        entity.page == '1'
        request.accountId == 'account-id'
        request.startDate == LocalDate.parse('2019-01-02')
        request.endDate == LocalDate.parse('2019-02-03')
        request.status == TransactionState.LookingIntoIt.searchKey
        request.page == 1
    }

    def "I can covert a request to a payload with nulls"() {
        given:
        TransactionListRequest request = new TransactionListRequest()
            .withStatus((TransactionState) null)
        when:
        Map<String, String> entity = mapper.writeToMap(request)

        then:
        entity.account_id == null
        entity.start_date == null
        entity.end_date == null
        entity.status == null
        entity.page == '0'
        request.accountId == null
        request.startDate == null
        request.endDate == null
        request.status == null
        request.page == 0
    }

}