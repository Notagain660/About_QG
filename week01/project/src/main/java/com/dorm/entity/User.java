package com.dorm.entity;

public class User {
    // 属性：和数据库表字段对应
    private String id;
    private String name;//从来没用到。
    private String password;
    private String role; // 角色：student 或 admin
    private String dormBuilding; // 宿舍楼，可为空
    private String roomNumber;   // 房间号，可为空

    // 必须有一个空构造方法
    public User() {
    }

    // Getter 和 Setter 方法（右键 -> Generate -> Getter and Setter 可以自动生成）
    // 这是让其他代码能读取和修改这些私有属性的标准写法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDormBuilding() {
        return dormBuilding;
    }

    public void setDormBuilding(String dormBuilding) {
        this.dormBuilding = dormBuilding;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    // 为了方便看结果，可以重写 toString 方法
    @Override
    public String toString() {//不是给用户看的
        return "User{" +
                "学号/工号=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", dormBuilding='" + dormBuilding + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }
}