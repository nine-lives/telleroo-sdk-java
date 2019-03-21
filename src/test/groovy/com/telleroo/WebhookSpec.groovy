package com.telleroo

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import org.joda.time.DateTime
import spock.lang.Specification

class WebhookSpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can covert a JSON payload to the lead webhook"() {
        given:
        String payload = '''
                {
                  "webhook": {
                    "company_id": "538633c5-d120-45fa-9ee5-1c6a348232",
                    "company_name": "Test Company One"
                  }
                }
       '''

        when:
        Webhook<Company> entity = mapper.readValue(payload, new TypeReference<Webhook<Company>>() {})

        then:
        entity.webhook.companyId ==  '538633c5-d120-45fa-9ee5-1c6a348232'
        entity.webhook.companyName ==  'Test Company One'
    }
}
