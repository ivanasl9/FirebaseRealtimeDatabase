package com.example.firebaserealtimedatabaserecyclerviewjava;

public class Data {

    String firstName, lastName, url, username;

    public Data() {
    }

    public Data(String firstName, String lastName, String url, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.url = url;
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
