package com.example.myapplication;

public class UserApplication {
    private String username;
    private String eventKey;

    public UserApplication() {
        // Default constructor for Firebase
    }

    public UserApplication(String username, String eventKey) {
        this.username = username;
        this.eventKey = eventKey;
    }

    public String getUsername() {
        return username;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
