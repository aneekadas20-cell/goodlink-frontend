package com.goodlink.app.model;

public class ActivityItem {
    private Long id;
    private String title;
    private String ngo;
    private String ngoName;
    private String date;
    private boolean joined;
    private String description;
    private String location;

    public ActivityItem() {
    }

    public ActivityItem(Long id, String title, String ngoName, String date) {
        this.id = id;
        this.title = title;
        this.ngoName = ngoName;
        this.date = date;
        this.joined = false;
    }

    public ActivityItem(Long id, String title, String ngoName, String date, boolean joined) {
        this.id = id;
        this.title = title;
        this.ngoName = ngoName;
        this.date = date;
        this.joined = joined;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNgoName() {
        if (ngoName != null && !ngoName.trim().isEmpty()) {
            return ngoName;
        }
        return ngo != null ? ngo : "";
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getDate() {
        return date != null ? date : "";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
