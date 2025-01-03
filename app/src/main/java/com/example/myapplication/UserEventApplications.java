package com.example.myapplication;
public class UserEventApplications {
    private String eventKey;
    private String username;

    // Default constructor
    public UserEventApplications() {
    }

    // Constructor with parameters
    public UserEventApplications(String eventKey, String username) {
        this.eventKey = eventKey;
        this.username = username;
    }

    // Getters and setters for eventKey and username
    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
