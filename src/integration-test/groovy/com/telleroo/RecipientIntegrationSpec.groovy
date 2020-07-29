package com.telleroo

class RecipientIntegrationSpec extends BaseIntegrationSpec {

    def "I can create and fetch a recipient"() {
        given:
        Recipient request = create()

        when:
        Recipient response = telleroo.createRecipient(request);

        then:
        response.name == request.name
        //response.sortCode == request.sortCode
        response.accountNo.substring(4) == request.accountNo.substring(4)
        response.legalType == request.legalType

        when:
        Recipient recipient = telleroo.getRecipient(response.id)

        then:
        recipient.name == request.name
        //recipient.sortCode == request.sortCode
        recipient.accountNo.substring(4) == request.accountNo.substring(4)
        recipient.legalType == request.legalType

        cleanup:
        if (response) {
            telleroo.deleteRecipient(response.id)
        }
    }

    def "I can list recipients"() {
        given:
        List<Recipient> recipients = (0..2).collect {
            telleroo.createRecipient(create())
        }

        when:
        RecipientList response = telleroo.getRecipients(1);

        then:
        response.recipients.find { it && it?.id == recipients[0].id }
        response.recipients.find { it && it?.id == recipients[1].id }
        response.recipients.find { it && it?.id == recipients[2].id }

        cleanup:
        if (recipients) {
            recipients.each { telleroo.deleteRecipient(it.id) }
        }
    }

    def "I can a 422 when I try to fetch an invalid id"() {
        when:
        telleroo.getRecipient("invalid-id")

        then:
        TellerooServerException e = thrown(TellerooServerException)
        e.statusCode == 422
        e.error.message == "Recipient does not exist"
    }

    def "I get an error if I try to create a recipient with incomplete details"() {
        when:
        Recipient response = telleroo.createRecipient(Recipient.builder()
                .withName("Bob")
                .withLegalType(LegalType.PRIVATE)
                .build());

        then:
        TellerooServerException e = thrown(TellerooServerException)
        e.statusCode == 422
        e.error.message == "Invalid request, check field errors"
        e.error.fieldErrors.size() == 1
        e.error.fieldErrors.currency_code.size() == 2
        e.error.fieldErrors.currency_code[0] == "can't be blank"
        e.error.fieldErrors.currency_code[1] == "is not included in the list"

        cleanup:
        if (response) {
            telleroo.deleteRecipient(response.id)
        }
    }

    static Recipient create() {
        Random rnd = new Random();
        Recipient.builder()
            .withName("Integration Spec")
            .withAccountNo(String.valueOf(rnd.nextInt(89999999) + 10000000))
            .withSortCode("123456")
            .withCurrencyCode("GBP")
            .withLegalType(rnd.nextInt() % 2 == 0? LegalType.PRIVATE : LegalType.BUSINESS)
            .build()
    }
}
