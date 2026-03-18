package com.dorm.test;

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
            UserMapper mapper = session.getMapper(UserMapper.class);

            // 4. 创建一个 User 对象
            User user = new User();
            user.setName("test001");
            user.setPassword("123456");
            user.setRole("student");

            // 5. 调用插入方法
            mapper.insertUser(user);
            System.out.println("插入成功，生成的主键 id = " + user.getId());

            // 6. 测试查询
            User u = mapper.selectById("3125004433");
            System.out.println("查询结果：" + u);
        }
    }
}