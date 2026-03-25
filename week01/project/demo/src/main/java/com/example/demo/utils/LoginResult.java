package com.example.demo.utils;

import com.example.demo.entity.User;

public record LoginResult(boolean success, User user, String message) {

    public static LoginResult success(User user) {
        return new LoginResult(true, user, null);
    }

    public static LoginResult fail(String message) {
        return new LoginResult(false, null, message);
    }
}
