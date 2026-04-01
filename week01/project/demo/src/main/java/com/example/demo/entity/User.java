package com.example.demo.entity;

import com.example.demo.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter//免于生成getter and setter
@Component
public class User {

    private String id;
    private String name;
    private String password;
    private Role role; // 角色：student 或 admin
    private String dormBuilding; // 宿舍楼，可为空
    private String roomNumber;   // 房间 号，可为空

    // 必须有一个空构造方法
    public User() {
    }

    // 为了方便看结果，可以重写 toString 方法
    @Override
    public String toString() {//不是给用户看的
        return "User{" +
                "学号/工号=" + id +
                "name=" + name + '\n' +
                "role=" + role + '\n' +
                "password=" + password + '\n' +
                "dormBuilding=" + dormBuilding + '\n' +
                "roomNumber=" + roomNumber + '\n' +
                '}';
    }
}