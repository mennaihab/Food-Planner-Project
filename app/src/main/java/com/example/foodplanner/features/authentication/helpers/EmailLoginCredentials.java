package com.example.foodplanner.features.authentication.helpers;

public class EmailLoginCredentials {
    private final String email;
    private final String password;

    public EmailLoginCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
