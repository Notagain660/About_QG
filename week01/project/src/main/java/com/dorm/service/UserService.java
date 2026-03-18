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

    public boolean register(String role, String id, String password) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            // 1. 检查用户名是否存在
            User existing = mapper.selectById(id);
            if (existing != null) {
                return false; // 用户名已存在
            }
            // 2. 创建新用户对象
            User user = new User();
            user.setId(id);
            user.setPassword(password);
            user.setPassword(password); // 暂时明文，后续可以加密
            user.setRole(role);
            // 3. 插入数据库
            mapper.insertUser(user);
            return true;
        }
    }

    }


