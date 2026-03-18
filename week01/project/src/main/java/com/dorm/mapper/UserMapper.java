package com.dorm.mapper;

import com.dorm.entity.User;

public interface UserMapper {
    // 插入一个用户
    void insertUser(User user);

    // 根据用户名查询用户（用于登录时检查密码）
    User selectById(String id);
}
