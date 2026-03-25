package com.example.demo.enums;

public enum Role
{
    STUDENT("学生"),
    ADMIN("管理员");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static Role fromChoice(int roleNumber) {
        return switch (roleNumber) {
            case 1 -> STUDENT;
            case 2 -> ADMIN;
            default -> throw new IllegalArgumentException("无效角色选择");
        };
    }
}
