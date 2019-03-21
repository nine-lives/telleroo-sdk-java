package com.telleroo

import com.fasterxml.jackson.databind.ObjectMapper
import com.telleroo.util.ObjectMapperFactory
import com.telleroo.util.RequestParameterMapper
import spock.lang.Specification

class UserCreateRequestSpec extends Specification {
    private RequestParameterMapper mapper = new RequestParameterMapper()

    def "I can covert a request to a payload"() {
        given:
        UserCreateRequest request = new UserCreateRequest()
                .withEmail("test@test.com")
                .withPhoneNumber("787555555")
                .withPhoneNumberCountryCode("44")
                .withPassword("password123")
        when:
        Map<String, String> entity = mapper.writeToMap(request)

        then:
        request.email == 'test@test.com'
        entity.email == 'test@test.com'
        request.phoneNumber == '787555555'
        entity.phone_number == '787555555'
        request.phoneNumberCountryCode == '44'
        entity.phone_number_country_code == '44'
        request.password == 'password123'
        entity.password == 'password123'

        when:
        ObjectMapper om = ObjectMapperFactory.make()
        UserCreateRequest result = om.readValue(om.writeValueAsString(entity), UserCreateRequest)

        then:
        result.email == request.email
    }
}