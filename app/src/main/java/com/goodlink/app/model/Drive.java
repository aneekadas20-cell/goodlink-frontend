package com.goodlink.app.model;

public class Drive {
    private Long id;
    private String title;
    private String organizer;
    private String ngoName;
    private String date;
    private String description;
    private String location;

    public Drive() {
    }

    public Drive(Long id, String title, String organizer, String date, String description, String location) {
        this.id = id;
        this.title = title;
        this.organizer = organizer;
        this.date = date;
        this.description = description;
        this.location = location;
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

    public String getOrganizer() {
        if (organizer != null && !organizer.trim().isEmpty()) {
            return organizer;
        }
        return ngoName != null ? ngoName : "";
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getDate() {
        return date != null ? date : "";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location != null ? location : "";
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
