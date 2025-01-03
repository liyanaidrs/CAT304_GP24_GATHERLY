package com.example.myapplication;
public class ApplicationData {
    private String username;
    private String image;
    private String title;
    private String description;
    private String language;
    private String date;
    private String eventKey;
    private String c1, c2, c3;
    public ApplicationData() {
        // Default constructor for Firebase
    }

    public ApplicationData(String username, String image, String title, String description, String language, String date, String eventKey, String c1, String c2, String c3) {
        this.username = username;
        this.image = image;
        this.title = title;
        this.description = description;
        this.language = language;
        this.date = date;
        this.eventKey = eventKey;
        this.c1=c1;
        this.c2=c2;
        this.c3=c3;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getDate() {
        return date;
    }


    public String getEventKey() {
        return eventKey;
    }
    public void setkey(String key){
        this.eventKey=key;
    }
}