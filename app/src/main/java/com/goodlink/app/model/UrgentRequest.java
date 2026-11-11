package com.goodlink.app.model;

public class UrgentRequest {
    private Long id;
    private String type; // e.g. "BLOOD" or "FOOD"
    private String details;
    private String city;
    private String email;
    private String date;
    private String status;
    private Integer unitsNeeded;
    private String hospitalName;
    private String reason;
    private String contactPhone;
    @com.google.gson.annotations.SerializedName("requestId")
    private Long requestId;
    private java.util.List<PotentialDonor> potentialDonors;

    public UrgentRequest() {
    }

    public UrgentRequest(String type, String details, String city, String email) {
        this.type = type;
        this.details = details;
        this.city = city;
        this.email = email;
    }

    public UrgentRequest(String type, String details, String city, String email, Integer unitsNeeded, String hospitalName, String reason, String contactPhone) {
        this.type = type;
        this.details = details;
        this.city = city;
        this.email = email;
        this.unitsNeeded = unitsNeeded;
        this.hospitalName = hospitalName;
        this.reason = reason;
        this.contactPhone = contactPhone;
    }

    public UrgentRequest(Long id, String type, String details, String city, String email, String date, String status) {
        this.id = id;
        this.type = type;
        this.details = details;
        this.city = city;
        this.email = email;
        this.date = date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type != null ? type : "BLOOD";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details != null ? details : "";
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCity() {
        return city != null ? city : "";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date != null ? date : "";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status != null ? status : "URGENT";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUnitsNeeded() {
        return unitsNeeded != null ? unitsNeeded : 1;
    }

    public void setUnitsNeeded(Integer unitsNeeded) {
        this.unitsNeeded = unitsNeeded;
    }

    public String getHospitalName() {
        return hospitalName != null ? hospitalName : "Not specified";
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getReason() {
        return reason != null ? reason : "Not specified";
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContactPhone() {
        return contactPhone != null ? contactPhone : "Not provided";
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Long getRequestId() {
        return requestId != null ? requestId : id;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public java.util.List<PotentialDonor> getPotentialDonors() {
        return potentialDonors;
    }

    public void setPotentialDonors(java.util.List<PotentialDonor> potentialDonors) {
        this.potentialDonors = potentialDonors;
    }
}
