package com.telleroo

import spock.lang.Specification

class TellerooServerExceptionSpec extends Specification {

    def "I can construct the exception"() {
        given:
        TellerooError error = new TellerooError("error_message")

        when:
        TellerooServerException e = new TellerooServerException(401, 'Unauthorised', error)

        then:
        e.statusCode == 401
        e.statusMessage == 'Unauthorised'
        e.error.message == 'error_message'
    }
}
