package com.example.demo.entity;

public class LoginreturnData {
    private String token;
    private User user;

    public LoginreturnData(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
