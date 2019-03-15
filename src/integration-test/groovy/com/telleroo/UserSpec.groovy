package com.telleroo

class UserSpec extends BaseIntegrationSpec {

    def "I can create and fetch a user"() {
        given:
        UserCreateRequest request = create()
        when:
        User response = telleroo.createUser(request)

        then:
        response.email == request.email

        when:
        User user = telleroo.getUser(response.id)

        then:
        user.id == response.id
        user.email == request.email

        cleanup:
        if (response) {
            telleroo.deleteUser(response.id)
        }
    }

    def "I can list users"() {
        given:
        List<User> users = (0..2).collect {
            telleroo.createUser(create())
        }

        when:
        UserList response = telleroo.getUsers()

        then:
        response.users.find { it && it?.id == users[0].id }
        response.users.find { it && it?.id == users[1].id }
        response.users.find { it && it?.id == users[2].id }

        cleanup:
        if (users) {
            users.each { telleroo.deleteUser(it.id) }
        }
    }

    private static UserCreateRequest create() {
        Random rnd = new Random()
        new UserCreateRequest()
            .withEmail("integrationspec+" + rnd.nextInt() + "@test.com")
            .withPhoneNumber("787" + String.valueOf(rnd.nextInt(8999999) + 1000000L))
            .withPhoneNumberCountryCode("44")
            .withPassword("password123")
    }
}
