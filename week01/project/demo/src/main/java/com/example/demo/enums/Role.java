package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum Role
{
    ADMIN("管理员");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

}
