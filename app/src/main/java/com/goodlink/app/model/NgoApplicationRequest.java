package com.goodlink.app.model;

public class NgoApplicationRequest {
    private Long ngoId;
    private String name;
    private String email;
    private String message;

    public NgoApplicationRequest() {
    }

    public NgoApplicationRequest(Long ngoId, String name, String email, String message) {
        this.ngoId = ngoId;
        this.name = name;
        this.email = email;
        this.message = message;
    }

    public Long getNgoId() {
        return ngoId;
    }

    public void setNgoId(Long ngoId) {
        this.ngoId = ngoId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
