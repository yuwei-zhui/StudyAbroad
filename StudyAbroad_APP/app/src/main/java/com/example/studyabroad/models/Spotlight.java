package com.example.studyabroad.models;

public class Spotlight {
    private String title;
    private int backgroundColor;
    private int iconResId;

    public Spotlight(String title, int backgroundColor, int iconResId) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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