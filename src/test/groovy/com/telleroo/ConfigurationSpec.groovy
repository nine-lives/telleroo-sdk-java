package com.telleroo

import spock.lang.Specification

class ConfigurationSpec extends Specification {

    private String version

    def setup() {
        Properties versionProperties = new Properties()
        versionProperties.load(Configuration.class.getClassLoader().getResourceAsStream("version.properties"))
        version = versionProperties.getProperty("version")
    }

    def "The defaults are the defaults"() {
        when:
        Configuration config = new Configuration()

        then:
        config.apiKey == null
        config.endpoint == 'https://api.telleroo.com/'
        config.maxConnectionsPerRoute == 20
        config.userAgent == "telleroo-java-sdk/${version}".toString()
        config.requestBurstSize == 20
        config.requestsPerSecond == 5
        !config.blockTillRateLimitReset
        version ==~ /1\.\d+\.\d+/
    }

    def "I can set configuration values"() {
        when:
        Configuration config = new Configuration()
            .withApiKey("secret")
            .withEndpoint("https://bpi.telleroo.com/")
            .withMaxConnectionsPerRoute(22)
            .withUserAgent("ninelives/9.0.0")
            .withBlockTillRateLimitReset(true)
            .withRequestBurstSize(25)
            .withRequestsPerSecond(10)

        then:
        config.apiKey == 'secret'
        config.endpoint == 'https://bpi.telleroo.com/'
        config.maxConnectionsPerRoute == 22
        config.userAgent == "ninelives/9.0.0 telleroo-java-sdk/${version}".toString()
        config.requestBurstSize == 25
        config.requestsPerSecond == 10
        config.blockTillRateLimitReset
    }

    def "I can set and unset the user agent"() {
        when:
        Configuration config = new Configuration()

        then:
        config.userAgent == "telleroo-java-sdk/${version}".toString()

        when:
        config.withUserAgent("ninelives/9.0.0")

        then:
        config.userAgent == "ninelives/9.0.0 telleroo-java-sdk/${version}".toString()

        when:
        config.withUserAgent(null)

        then:
        config.userAgent == "telleroo-java-sdk/${version}".toString()

        when:
        config.withUserAgent(" ")

        then:
        config.userAgent == "telleroo-java-sdk/${version}".toString()
    }

}
