package com.goodlink.app.model;

public class RegisterRequest {
    private String name;
    private String email;
    private String bloodType;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String bloodType) {
        this.name = name;
        this.email = email;
        this.bloodType = bloodType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
