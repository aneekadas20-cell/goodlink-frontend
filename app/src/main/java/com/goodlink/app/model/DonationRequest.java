package com.goodlink.app.model;

public class DonationRequest {
    private String email;
    private String item;
    private Integer quantity;

    private String category;
    private String condition;
    private String deliveryMethod;
    private String pickupAddress;
    private String preferredTime;

    public DonationRequest() {
    }

    public DonationRequest(String email, String item, Integer quantity) {
        this.email = email;
        this.item = item;
        this.quantity = quantity;
    }

    public DonationRequest(String email, String item, Integer quantity, String category, String condition, String deliveryMethod, String pickupAddress, String preferredTime) {
        this.email = email;
        this.item = item;
        this.quantity = quantity;
        this.category = category;
        this.condition = condition;
        this.deliveryMethod = deliveryMethod;
        this.pickupAddress = pickupAddress;
        this.preferredTime = preferredTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity != null ? quantity : 1;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
