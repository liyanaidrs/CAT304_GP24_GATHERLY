package com.example.myapplication;

public class EventCheckpoints {
    private String username;
    private String eventKey;
    private String eventName;
    private boolean checkpoint1;
    private boolean checkpoint2;
    private boolean checkpoint3;
    private boolean eventcompleted;

    // Constructor
    public EventCheckpoints(String username, String eventKey, String eventName) {
        this.username = username;
        this.eventKey = eventKey;
        this.eventName = eventName;
        this.checkpoint1 = false; // Initially set to false
        this.checkpoint2 = false; // Initially set to false
        this.checkpoint3 = false;
        this.eventcompleted=false;// Initially set to false
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean isCheckpoint1() {
        return checkpoint1;
    }

    public void setCheckpoint1(boolean checkpoint1) {
        this.checkpoint1 = checkpoint1;
    }

    public boolean isCheckpoint2() {
        return checkpoint2;
    }

    public void setCheckpoint2(boolean checkpoint2) {
        this.checkpoint2 = checkpoint2;
    }

    public boolean isCheckpoint3() {
        return checkpoint3;
    }

    public void setCheckpoint3(boolean checkpoint3) {
        this.checkpoint3 = checkpoint3;
    }
    public boolean EventCompleted() {
        return eventcompleted;}

    public void setCompleted (boolean eventCompleted) {
        this.eventcompleted = eventCompleted;
    }

    // toString method for debugging or logging
    @Override
    public String toString() {
        return "EventCheckpoints{" +
                "username='" + username + '\'' +
                ", eventKey='" + eventKey + '\'' +
                ", eventName='" + eventName + '\'' +
                ", checkpoint1=" + checkpoint1 +
                ", checkpoint";
    }}