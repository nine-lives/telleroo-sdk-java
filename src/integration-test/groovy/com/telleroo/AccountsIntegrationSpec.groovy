package com.telleroo

class AccountsIntegrationSpec extends BaseIntegrationSpec {

    def "I can get a list of accounts"() {
        when:
        AccountsList response = telleroo.getAccounts()
        println response.accounts[0].balance

        then:
        response.accounts.size() > 0
        with(response.accounts[0]) {
            id ==~ /[A-z0-9\-]*/
            name
            status
            currencyCode.length() == 3
            accountNo
            sortCode ==~ /\d{6}/
        }
    }
}
