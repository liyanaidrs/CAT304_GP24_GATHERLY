package com.example.myapplication;  // Ensure this matches your app's package

public class User {
    private String username;

    // Constructor
    public User(String username) {
        this.username = username;
    }

    // Getter method
    public String getUsername() {
        return username;
    }

    // Setter method (optional, if you need to modify username)
    public void setUsername(String username) {
        this.username = username;
    }
}
