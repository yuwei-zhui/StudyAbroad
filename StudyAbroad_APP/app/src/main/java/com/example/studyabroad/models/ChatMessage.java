package com.example.studyabroad.models;

public class ChatMessage {
    public static final int TYPE_USER = 0;
    public static final int TYPE_AI = 1;

    private long id;
    private String message;
    private long timestamp;
    private boolean isUser;

    // Constructor 1: for loading messages from database
    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
        this.timestamp = System.currentTimeMillis();
    }

    // Constructor 2: for creating new messages
    public ChatMessage(String message, long timestamp, boolean isUser) {
        this.message = message;
        this.timestamp = timestamp;
        this.isUser = isUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public int getType() {
        return isUser ? TYPE_USER : TYPE_AI;
    }
} 