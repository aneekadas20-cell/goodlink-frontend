package com.goodlink.app.model;

import androidx.annotation.NonNull;

public class Ngo {
    private Long id;
    private String name;
    private String description;
    private String city;

    public Ngo() {
    }

    public Ngo(Long id, String name, String description, String city) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
