package com.example.demo.service;

import com.example.demo.enums.Role;
import com.example.demo.utils.LoginResult;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public boolean register(Role role, String id, String name, String password) {

        // 1. 检查用户名是否存在
        User existing = userMapper.selectById(id);
        if (existing != null) {
            return false; // 用户名已存在
        }
        // 2. 创建新用户对象
        User user = new User();
        user.setRole(role);
        user.setId(id);
        user.setName(name);
        user.setPassword(password); // 暂时明文，后续可以加密

        // 3. 插入数据库
        userMapper.insertUser(user);
        return true;
    }

    public LoginResult login(String id, String password) {

            // 检查用户名是否存在
            User existing =userMapper.selectById(id);

            if (existing == null) {
                return LoginResult.fail("用户不存在");
            }else if(!existing.getPassword().equals(password)) {
                return LoginResult.fail("密码错误");
            }else{
                return LoginResult.success(existing);
            }
        }



    public boolean updater(User currentUser, String dormBuilding, String roomNumber, String password) {

            String previousRoomNumber = currentUser.getRoomNumber();

            currentUser.setDormBuilding(dormBuilding);
            currentUser.setRoomNumber(roomNumber);
            currentUser.setPassword(password);
            userMapper.updateUser(currentUser);

            String newRoomNumber = currentUser.getRoomNumber();

            return !newRoomNumber.equals(previousRoomNumber);


    }
}
