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
}