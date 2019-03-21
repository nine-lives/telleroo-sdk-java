package com.telleroo

import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import spock.lang.Specification

class UserSpec extends Specification {
    private ObjectMapper mapper = ObjectMapperFactory.make()

    def "I can covert a JSON payload to the entity"() {
        given:
        String payload = '''
                {
                    "user": {
                        "id": "b357b7fe-2ea5-49a7-828c-76a0850d771d",
                        "email": "sam@test.com",
                        "status": "initialized"
                    }
                }
       '''

        when:
        User entity = mapper.readValue(payload, User.UserWrapper).getUser();

        then:
        entity.id == 'b357b7fe-2ea5-49a7-828c-76a0850d771d'
        entity.email == 'sam@test.com'
        entity.status == 'initialized'
    }
}