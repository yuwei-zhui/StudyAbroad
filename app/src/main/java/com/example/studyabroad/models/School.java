package com.example.studyabroad.models;

public class School {
    private String name;
    private String location;
    private int ranking;
    private int backgroundColor;
    private int iconResId;

    public School(String name, String location, int ranking, int backgroundColor, int iconResId) {
        this.name = name;
        this.location = location;
        this.ranking = ranking;
        this.backgroundColor = backgroundColor;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
} 