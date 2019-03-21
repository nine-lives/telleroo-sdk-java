package com.telleroo

import spock.lang.Specification
import spock.lang.Unroll

class WebhookTypeSpec extends Specification {

    @Unroll("I can parse the value into a WebhookType - #value")
    def "I can parse the value into a WebhookType"() {
        when:
        WebhookType type = WebhookType.getType(value)

        then:
        type.value == value
        type == WebhookType.getType(type.name())

        where:
        value <<
        ['New Credit',
        'Failed Payment',
        'Sent Payment',
        'Company Approved']

    }
}
