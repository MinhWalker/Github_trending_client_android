package com.example.githubtrendingclient.Models.Req;

public class UserUpdate {
    private String fullName;
    private String email;

    public UserUpdate() {
    }

    public UserUpdate(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
