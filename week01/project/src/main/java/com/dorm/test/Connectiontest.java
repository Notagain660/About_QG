package com.dorm.test;

import com.dorm.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Connectiontest {
    public static void main(String[] args) {
        SqlSession session = null;
        try {
            // 1. 读取核心配置文件（MyBatis最标准的写法）[citation:4]
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);

            // 2. 根据配置文件，创建SqlSessionFactory（数据库会话工厂）
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            // 3. 打开一个会话（相当于拿到数据库连接）
            session = sqlSessionFactory.openSession();

            // 4. 如果能走到这里没报错，恭喜你，数据库连接成功了！
            System.out.println("✅✅✅ 恭喜！数据库连接成功！✅✅✅");

            // 5. （可选）试试能不能查询点东西来彻底验证
            // 因为还没写Mapper，这里先注释掉
            // List<User> users = session.selectList("com.dorm.mapper.UserMapper.selectAll");
            // System.out.println("查询到的用户数量: " + users.size());

        } catch (Exception e) {
            // 如果报错了，会进到这里来
            System.out.println("❌❌❌ 数据库连接失败，请检查配置。错误信息如下：");
            e.printStackTrace(); // 打印详细的错误信息
        } finally {
            // 6. 无论成功失败，最后都要关闭会话，释放资源
            if (session != null) {
                session.close();
            }
        }
    }
}
