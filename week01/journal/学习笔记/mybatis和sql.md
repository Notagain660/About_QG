
参考：菜鸟教程、廖雪峰的sql教程
## 关于MyBatis的理解

 - MyBatis 是一个ORM（对象关系映射）框架；
 - 作用： 在Java中通过MyBatis（相当于接口）操作数据库，替代JDBC代码

- **类比C语言：**  
- 相当于写了一个库，它帮你把数据库操作封装成函数；
- 调用定义了接口的对象，它自动处理连接、语句、返回的结果


## MySQL基本操作

环境配置：安装选custom保证必须配件否则可能无法运行

最简单修补：再次下载后先remove
使用workbench或者命令行操作

win +R输入regedit进入注册表

主机用户登录：`mysql -u root -p`
Enter password: 
***********

密码错误：ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password: YES)


登录成功：Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 35
Server version: 8.0.45 MySQL Community Server - GPL

Copyright (c) 2000, 2026, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

查看版本：mysql -version

安全配置：mysql_secure_installation


## SQL基本语法

查看全部数据库：`SHOW DATABASES`;
````{
+--------------------+
| Database           |
+--------------------+
| dorm_repair        |
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
}
````

调用数据库：`use dorm_repair（数据库名）;`
`Database changed`

建表：`CREATE TABLE tablename(
,,,,
);

查表：` SELECT * FROM user（表名）;

````
```
+------------+---------+----------+---------+--------------+------------+
| id         | name    | password | role    | dormBuilding | roomNumber |
+------------+---------+----------+---------+--------------+------------+
| 3225004433 | test001 | 123456   | student | NULL         | NULL       |
+------------+---------+----------+---------+--------------+------------+
````

清空数据表： TRUNCATE TABLE user（表名）;
注意：无法删除被映射的表
````
```
ERROR 1701 (42000): Cannot truncate a table referenced in a foreign key constraint (`dorm_repair`.`repairorder`, CONSTRAINT `repairorder_ibfk_1`)
````

删除数据表内容：`DELETE FROM repairorder（表名）;
注意：有映射关系的表先删子表再删母表

展示时区：SHOW VARIABLES LIKE '%time_zone%';