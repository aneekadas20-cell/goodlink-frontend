package com.goodlink.app.model;

public class Achievement {
    private Long id;
    private String title;
    private String description;
    private String volunteerName;
    private String recipient;
    private String date;
    private String icon;

    public Achievement() {
    }

    public Achievement(Long id, String title, String description, String volunteerName, String date, String icon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.volunteerName = volunteerName;
        this.date = date;
        this.icon = icon;
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

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVolunteerName() {
        if (volunteerName != null && !volunteerName.trim().isEmpty()) {
            return volunteerName;
        }
        return recipient != null ? recipient : "";
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String getDate() {
        return date != null ? date : "";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon != null ? icon : "🏆";
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
