package com.dorm.service;

import com.dorm.enums.Role;
import org.apache.ibatis.exceptions.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

import com.dorm.entity.User;
import com.dorm.mapper.UserMapper;
import com.dorm.utils.LoginResult;

public class UserService {
    private final SqlSessionFactory sqlSessionFactory;

    public UserService() {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean register(Role role, String id, String name, String password) {
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
        }catch (PersistenceException e) {
            // 记录日志
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            // 转换为运行时异常，让上层统一处理
            throw new RuntimeException("注册失败，请稍后重试", e);
        }

    }

    public LoginResult login(String id, String password) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            // 检查用户名是否存在
            User existing = mapper.selectById(id);

            if (existing == null) {
                return com.dorm.utils.LoginResult.fail("用户不存在");
            }else if(!existing.getPassword().equals(password)) {
                return com.dorm.utils.LoginResult.fail("密码错误");
            }else{
                return com.dorm.utils.LoginResult.success(existing);
            }
        }catch (PersistenceException e) {
            // 数据库操作异常（如连接失败、SQL错误）
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            return com.dorm.utils.LoginResult.fail("系统繁忙，请稍后再试。");
        } catch (Exception e) {
            // 其他未预料异常
            System.err.println("未知错误：" + e.getMessage());
            e.printStackTrace();
            return com.dorm.utils.LoginResult.fail("操作失败，请联系管理员或等待系统修复。");
        }

    }

    public boolean updater(User currentUser, String dormBuilding, String roomNumber, String password) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            String previousRoomNumber = currentUser.getRoomNumber();

            currentUser.setDormBuilding(dormBuilding);
            currentUser.setRoomNumber(roomNumber);
            currentUser.setPassword(password);
            mapper.updateUser(currentUser);

            String newRoomNumber = currentUser.getRoomNumber();

            return !newRoomNumber.equals(previousRoomNumber);

        }catch (PersistenceException e) {
            // 记录日志
            System.err.println("数据库操作失败：" + e.getMessage());
            e.printStackTrace();
            // 转换为运行时异常，让上层统一处理
            throw new RuntimeException("更新失败，请稍后重试", e);
        }
    }

    }


