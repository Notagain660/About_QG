package com.dorm.test;

import java.util.*;
import java.time.Instant;
import com.dorm.entity.Repairorder;
import com.dorm.enums.Role;
import com.dorm.mapper.RepairorderMapper;
import com.dorm.entity.User;
import com.dorm.mapper.UserMapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class testUserMapper {
    public static void main(String[] args) throws Exception {
        // 1. 加载 MyBatis 配置文件
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 2. 打开一个 SqlSession（true 表示自动提交事务）
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            // 3. 获取 Mapper 接口的代理对象
            UserMapper mapper = session.getMapper(UserMapper.class);//...
            RepairorderMapper repairorderMapper = session.getMapper(RepairorderMapper.class);

            // 4. 创建一个 User 对象
            User user = new User();
            user.setId("3225004433");
            user.setName("test001");
            user.setPassword("123456");
            user.setRole(Role.STUDENT);

            Repairorder repairorder = new Repairorder();
            repairorder.setId("3225004433");
            repairorder.setCreateTime(Date.from(java.time.Instant.now()));//new Date()可以



            // 5. 调用插入方法
            mapper.insertUser(user);
            System.out.println("插入成功，生成的主键 id = " + user.getId());
            repairorderMapper.insertRepairorder(repairorder);
            System.out.println("插入成功，生成的主键 id = " + repairorder.getOrderId());

            // 6. 测试查询
            User u = mapper.selectById("3225004433");
            System.out.println("查询结果：" + u);
            Repairorder r = repairorderMapper.selectByOrderId(repairorder.getOrderId());
            System.out.println("查询结果：" + r);
        }
    }
}