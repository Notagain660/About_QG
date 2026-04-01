
参考：菜鸟教程、廖雪峰的sql教程


## 关于MyBatis的理解

 - MyBatis 是一个ORM（对象关系映射）框架；
 - 作用： 在Java中通过MyBatis（相当于接口）操作数据库，替代JDBC代码

- **类比C语言：**  
- 相当于写了一个库，它帮你把数据库操作封装成函数；
- 调用定义了接口的对象，它自动处理连接、语句、返回的结果


## MySQL基本操作

环境配置：安装选custom保证必须配件否则可能无法运行

win +R输入regedit进入注册表

主机用户登录：`mysql -u root -p`
Enter password: 

卸载注意：根目录要删干净，注册表也要删，最快的方法是重新下载然后remove

手动配置：管理员权限cmd，`cd /d D:\MySQL\bin`

环境变量：**Path**  **添加路径**，点击“新建”，粘贴**复制 MySQL 的 `bin` 目录路径**（例如 `D:\MySQL\MySQL Server 8.0\bin`

初始化数据目录：`mysqld --initialize --console`(记得记录临时密码)

安装MySQL服务：`mysqld --install`

启动：net start mysql

修改密码：ALTER USER 'root'@'localhost' IDENTIFIED BY '你的新密码';

重置密码: `mysqld --skip-grant-tables` 

退出 MySQL 命令行: `exit;`

**URL** 是 `jdbc:mysql://localhost:3306/数据库名`


报错信息：
1. 安装完**检查 Windows 服务** ：按 `Win + R`，输入 `services.msc` 回车，在服务列表里找 **MySQL**（可能叫 `MySQL` 或 `MySQL80`）没找到MySQL，bin文件夹也没找到或者与其它的文件夹分开了：安装失败，建议重新安装

2. 安装完2753错误码：
    - 使用 MySQL Installer 自带的 **Remove** 功能彻底卸载残留
    - 重新打开 MySQL Installer（如果它自动弹出，选择 **Remove**）。
    - 在组件列表里，**勾选所有带 MySQL 的项**，点击 **Remove**，等它卸载完成。
    - 卸载后，重启电脑（**重要**，可以释放文件锁）。
    - 清除根目录和注册表
    - 清理 Windows Installer 缓存，有时旧的安装缓存会导致新安装失败。
    1. 【按 `Win + R`，输入 `cmd` 打开命令提示符（**以管理员身份运行**）】。
    2. 输入以下命令，停止 Windows Installer 服务：
       net stop msiserver
    3. 删除缓存文件夹：打开 `C:\Windows\Installer`，删除其中文件名包含 `MySQL` 的 MSI 文 件（如果有）。注意这个文件夹是受保护的，可能需要额外权限，如果无法删除就跳过。
    4. 重新启动 Windows Installer 服务：
       net start msiserver

3. C:\Users\36007>net stop msiserver System error 5 has occurred. Access is denied.
    - 权限不足，用管理员身份运行

4. 语句必须有;结尾，即使换行了

5. C:\Users\36007>mysql -u root -p 'mysql' is not recognized as an internal or external command, operable program or batch file，命令行找不到MySQL客户端程序，配置环境变量或者在bin目录下执行







**连接数据库**：
一：
6. 打开数据库工具窗口：IDEA 找到右侧边的 数据库 图标。如果找不到，可以通过菜单栏 视图 -> 工具窗口 -> `Database` 打开 [](https://www.jetbrains.com.cn/help/idea/mysql.html#connection_settings)[](https://www.jetbrains.com.cn/en-us/help/idea/2025.2/quick-start-with-database-functionality.html)。
7. 新建数据源：在 Database 窗口点击 **“+”** 号，选择数据源，然后选择 MySQL [](https://www.jetbrains.com.cn/help/idea/mysql.html#connection_settings)。
8. 填写信息：在打开的“数据源和驱动程序”对话框中，填写与上面类似的连接信息 [](https://www.jetbrains.com.cn/help/idea/mysql.html#connection_settings)：
    - 主机：`127.0.0.1` 或 `localhost`
    - 端口：`3306`
    - 用户：`root`
    - 密码： 
    - 数据库：这里可以先不填，连接上之后可以选择。 
9. **下载驱动（如果是第一次）**：如果界面下方有 **“Download missing driver files”** 的链接，点击它以下载连接 MySQL 所需的驱动程序 [](https://www.jetbrains.com.cn/help/idea/mysql.html#connection_settings)[](https://www.jetbrains.com.cn/en-us/help/idea/2025.2/quick-start-with-database-functionality.html)。
10. **测试连接**：填写完后，点击下方的 **“Test Connection”** 按钮。成功后会弹出提示框，点击“OK”或“Apply”保存即可 [](https://www.jetbrains.com.cn/help/idea/mysql.html#connection_settings)。

二、mybatis-config.xml中
````{<dataSource type="POOLED">
    <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/dorm_repair?useSSL=false&amp;serverTimezone=UTC"/>
    <property name="username" value="root"/>
    <property name="password" value="你的密码"/>
</dataSource>
}
````
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

创建数据库：CREATE DATABASE 数据库名;

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

建表：`CREATE TABLE tablename(`
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
注意：无法清空被映射的表
````
```
ERROR 1701 (42000): Cannot truncate a table referenced in a foreign key constraint (`dorm_repair`.`repairorder`, CONSTRAINT `repairorder_ibfk_1`)
````

删除数据表内容：`DELETE FROM repairorder（表名）;
注意：有映射关系的表先删子表再删母表

数据库的 `UPDATE` 操作就是覆盖原有字段的值，没写的不变

展示时区：SHOW VARIABLES LIKE '%time_zone%';
数据类型： INT, VARCHAR, DATE, BOOLEAN，你可以根据实际需要选择不同的数据类型。AUTO_INCREMENT 关键字用于创建一个自增长的列，PRIMARY KEY 用于定义主键（不能重复， 不能为空（NOT NULL），一张表只能有一个主键），NOT NULL不能为空，UNIQUE不能重复，==TEXT,  DATETIME DEFAULT CURRENT_TIMESTAMP, ON UPDATE CURRENT_TIMESTAMP,==

MyBatis 默认不会自动将下划线映射为驼峰，需要开启 `mapUnderscoreToCamelCase` 或者在 XML 中直接使用与实体类相同的属性名。
**解决方案**：
- 在 `mybatis-config.xml` 中添加设置：
<settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings> 
- 或者在 XML 中直接使用实体类属性名（推荐，避免歧义）

`ORDER BY orderId` 以保证顺序

更新：UPDATE user SET role = UPPER(role); 

#### MyBatis库提供的类和方法：
1. **`nputStream inputStream = Resources.getResourceAsStream(resource);`**
    - **知识点**：MyBatis 的 `Resources` 工具类、类路径资源加载、IO流。
    - **作用**：使用 MyBatis 提供的 `Resources.getResourceAsStream()` 方法从类路径中加载配置文件，返回一个 `InputStream` 输入流对象。这个流包含了配置文件的内容。
        
2. **`sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);`**
    - **知识点**：MyBatis 的 `SqlSessionFactoryBuilder`、构建器模式。
    - **作用**：创建 `SqlSessionFactoryBuilder` 对象，并调用其 `build()` 方法，传入配置文件的输入流。`build()` 方法会解析配置文件（包括数据库连接信息、Mapper 映射等），最终返回一个 `SqlSessionFactory` 对象。这个工厂对象是 MyBatis 的核心，后续所有数据库操作都需要通过它来获取 `SqlSession`。

3. SqlSessionFactory：MyBatis 的核心对象，负责创建SqlSession（可以理解为一次数据库会话，就像一次打电话的过程）。你只需要在程序启动时根据配置文件（mybatis-config.xml）创建一个 SqlSessionFactory，然后每次需要操作数据库时，就用它打开一个 SqlSession，用完关闭。





















