package com.goodlink.app.model;

public class UrgentRespondRequest {
    private String email;
    private String bloodType;

    public UrgentRespondRequest() {
    }

    public UrgentRespondRequest(String email, String bloodType) {
        this.email = email;
        this.bloodType = bloodType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
