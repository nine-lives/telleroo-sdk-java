package com.telleroo;

public class UserCreateRequest {
    private String email;
    private String phoneNumberCountryCode;
    private String phoneNumber;
    private String password;

    public String getEmail() {
        return email;
    }

    public UserCreateRequest withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumberCountryCode() {
        return phoneNumberCountryCode;
    }

    public UserCreateRequest withPhoneNumberCountryCode(String phoneNumberCountryCode) {
        this.phoneNumberCountryCode = phoneNumberCountryCode;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserCreateRequest withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserCreateRequest withPassword(String password) {
        this.password = password;
        return this;
    }
}
