package com.telleroo;

public class User {
    private String id;
    private String email;
    private String status;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    static class UserWrapper {
        private User user;

        public User getUser() {
            return user;
        }
    }
}
