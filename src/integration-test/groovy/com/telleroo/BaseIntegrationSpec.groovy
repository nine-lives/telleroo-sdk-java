package com.telleroo

import com.telleroo.util.ObjectMapperFactory
import spock.lang.Specification

abstract class BaseIntegrationSpec extends Specification {

    protected static Telleroo telleroo

    def setupSpec() {
        ObjectMapperFactory.setFailOnUnknownProperties(true)
        telleroo = Telleroo.make(new Configuration()
                .withBlockTillRateLimitReset(true)
                .withRequestsPerSecond(1000)
                .withEndpoint(System.getProperty("tellerooEndpoint") ?: System.getenv("tellerooEndpoint") ?: "https://sandbox.telleroo.com")
                .withApiKey(System.getProperty("tellerooApiKey") ?: System.getenv("tellerooApiKey") ?: "df29f8d55c70fdaaf6a987a1bd38ea4d3dd2beeee76ab2ce147cc574ec6e1bfea8af4f66d84a229df3ca097b56e8a20d022393ef78329fecf189e8475f2716ac"))
    }
}
