package com.example.myapplication;

public class Event {
    private String eventKey;
    private String eventName;

    public Event(String eventKey, String eventName) {
        this.eventKey = eventKey;
        this.eventName = eventName;
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getEventName() {
        return eventName;
    }
}
