package com.telleroo

import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import spock.lang.Specification

class CompanySpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
            {
                "company_id": "538633c5-d120-45fa-9ee5-1c6a348232",
                "company_name": "Test Company One"
            }
       '''

        when:
        Company entity = mapper.readValue(payload, Company)

        then:
        entity.companyId == '538633c5-d120-45fa-9ee5-1c6a348232'
        entity.companyName == 'Test Company One'
    }
}