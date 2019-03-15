package com.telleroo

import com.telleroo.util.ObjectMapperFactory
import org.joda.time.DateTime
import org.joda.time.Minutes
import spock.lang.Specification

abstract class BaseIntegrationSpec extends Specification {

    protected static Telleroo telleroo

    def setupSpec() {
        ObjectMapperFactory.setFailOnUnknownProperties(true)
        telleroo = Telleroo.make(new Configuration()
                .withBlockTillRateLimitReset(true)
                .withRequestsPerSecond(1000)
                .withEndpoint(System.getProperty("tellerooEndpoint") ?: System.getenv("tellerooEndpoint") ?: "https://sandbox.telleroo.com")
                .withApiKey(System.getProperty("tellerooApiKey") ?: System.getenv("tellerooApiKey")))
    }
}
