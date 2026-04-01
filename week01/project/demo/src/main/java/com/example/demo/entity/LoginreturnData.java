package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginreturnData {
    private String token;
    private User user;

    public LoginreturnData(String token, User user) {
        this.token = token;
        this.user = user;
    }

}
