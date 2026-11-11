package com.goodlink.app.model;

public class JoinRequest {
    private String email;

    public JoinRequest() {
    }

    public JoinRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
