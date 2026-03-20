package com.dorm.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;
import com.dorm.entity.User;
import com.dorm.mapper.UserMapper;

public class UserService {
    private SqlSessionFactory sqlSessionFactory;

    public UserService() {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean register(String role, String id, String name, String password) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            // 1. 检查用户名是否存在
            User existing = mapper.selectById(id);
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
            mapper.insertUser(user);
            return true;
        }
    }

    public User login(String id, String password) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            // 检查用户名是否存在
            User existing = mapper.selectById(id);

            if (existing != null && existing.getPassword().equals(password)) {//比对密码
                return existing;
            }
        }
        return null;
    }

    public boolean updater(User currentUser, String dormBuilding, String roomNumber, String password) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            String previousPassword = currentUser.getPassword();
            String previousRoomNumber = currentUser.getRoomNumber();

            currentUser.setDormBuilding(dormBuilding);
            currentUser.setRoomNumber(roomNumber);
            currentUser.setPassword(password);
            mapper.updateUser(currentUser);

            String newPassword = currentUser.getPassword();
            String newRoomNumber = currentUser.getRoomNumber();

            if (!newPassword.equals(previousPassword)) {
                return true;
            }

            if (!newRoomNumber.equals(previousRoomNumber)) {
                return true;
            }

        }
        return false;
    }

    }


