package com.telleroo

import spock.lang.Specification

class TellerooExceptionSpec extends Specification {

    def "I can create an exception with just a message"() {
        when:
        TellerooException e = new TellerooException("error message")

        then:
        e.message == "error message"
    }

    def "I can create an exception with just a cause"() {
        given:
        IllegalArgumentException cause = new IllegalArgumentException();
        when:
        TellerooException e = new TellerooException(cause)

        then:
        e.cause == cause
    }

}
