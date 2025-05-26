package com.example.studyabroad.models;

public class AIRequest {
    private String message;
    private String userId;
    private String academicProfile;

    public AIRequest(String message, String userId, String academicProfile) {
        this.message = message;
        this.userId = userId;
        this.academicProfile = academicProfile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAcademicProfile() {
        return academicProfile;
    }

    public void setAcademicProfile(String academicProfile) {
        this.academicProfile = academicProfile;
    }
} 