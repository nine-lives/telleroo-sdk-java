package com.telleroo

import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import spock.lang.Specification

class TellerooErrorSpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can covert a JSON payload with field errors to the entity"() {
        given:
        String payload = '''
                {
                    "error": {
                        "field1": ["field1 error"],
                        "field2": ["field2 error"]
                    }
                }
       '''

        when:
        TellerooError entity = mapper.readValue(payload, TellerooError)

        then:
        entity.message == 'Invalid request, check field errors'
        entity.fieldErrors.size() == 2
        entity.fieldErrors.field1 == ['field1 error']
        entity.fieldErrors.field2 == ['field2 error']
    }

    def "I can covert a JSON payload with message error to the entity"() {
        given:
        String payload = '''
                {
                    "error": "serious malfunction"
                }
       '''

        when:
        TellerooError entity = mapper.readValue(payload, TellerooError)

        then:
        entity.message == 'serious malfunction'
        entity.error == 'serious malfunction'
        entity.fieldErrors.size() == 0
    }

}