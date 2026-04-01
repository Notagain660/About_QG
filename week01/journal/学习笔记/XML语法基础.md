 1. **配置数据源 (`dataSource`)**：
    
    - 紧接着，`<dataSource type="POOLED">`元素定义了如何连接到具体的数据库。`type="POOLED"`表示使用数据库连接池技术来提高性能。
        
    - **核心连接信息**：`<dataSource>`内部包含了四个`<property>`子元素。它们就像一个个填空项，每一项都通过`name`和`value`属性，提供了连接数据库必不可少的“钥匙”：
        
        - `driver`：指定了连接MySQL数据库的Java驱动程序类名。
            
        - `url`：这是连接数据库的**地址**。它指明了数据库位于本地（`localhost`），端口是`3306`，要连接的数据库名是`dorm_repair`。后面还跟着一些参数，如禁用SSL、设置时区。**注意**，这里原本的`&`字符在XML中必须写成`&amp;`，这是一种转义，教程中也提到了类似`&lt;`的转义规则。
            
        - `username`：连接数据库的用户名。
            
        - `password`：连接数据库的密码。
        -   ````
         {
        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url"value="jdbc:mysql:
        //localhost:3306/dorm_repair?      useSSL=false&amp;
        serverTimezone=UTC"/>
        } 
        ````   
2. **预留映射器位置 (`mappers`)**：   
    - 文件的最后，有一个被注释掉的`<mappers>`部分。它将在后续用于引入包含具体SQL语句的XML文件，将接口方法与SQL语句关联起来，是连接Java代码与数据库操作的桥梁。
    - ````{
      <!-- 2. 映射器配置：告诉MyBatis去哪找SQL语句（我们待会儿会创建） -->  
<!-- 这里先留空，等建好UserMapper.xml后再来填 -->  
<!-- <mappers>  
    <mapper resource="com/dorm/mapper/UserMapper.xml"/></mappers> -->}
      ````
    

3. **命名空间声明**：  
    - 命名空间通过`xmlns`属性（XML Namespace的缩写）来声明，语法如下：
    - **默认命名空间**：`xmlns="URI"`，表示该元素及其所有无前缀的子元素都属于这个URI标识的命名空间。
    - **带前缀的命名空间**：`xmlns:前缀="URI"`，声明一个前缀，之后凡是以“前缀:”开头的元素或属性，都属于该URI的命名空间。
    - URI通常是一个看起来像URL的字符串，但它**并不需要指向一个实际可访问的网页**，只是作为唯一标识符使用。
    - 这些是 XML **命名空间** 的声明。它们的作用是**唯一标识该 XML 文件中使用的标签词汇表**，避免不同 XML 语言中相同标签名产生冲突。`xsi:schemaLocation` 则指定了用于验证此文件结构的 **XSD 模式文档**的地址，相当于之前提到的 DTD 的替代方案，用来确保文件内容“合法”。
    - ````{
      <project xmlns="http://maven.apache.org/POM/4.0.0"  
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">}
      ````
4. **项目坐标**：   
    - `<groupId>org.example</groupId>`：定义了项目的**组织标识**，通常是公司或组织的域名倒写。
    - `<artifactId>week1</artifactId>`：定义了项目的**唯一模块名**，也就是这个项目的名称。
    - `<version>1.0-SNAPSHOT</version>`：定义了项目的**当前版本号**。`SNAPSHOT` 表示这是一个开发中的快照版本。
    - `<modelVersion>4.0.0</modelVersion>`：指明了这个 POM 模型本身的版本，对于 Maven 2 和 3 来说，固定为 4.0.0。
5. **项目属性**：   
    - `<properties>` 元素内部定义了一些可以被重复使用的属性，类似于编程中的变量。
    - `<maven.compiler.source>21</maven.compiler.source>` 和 `<maven.compiler.target>21</maven.compiler.target>`：告诉 Maven 的编译插件，项目的源代码应该使用 **Java 21** 的语法来编译，并且生成的字节码也应该兼容 Java 21 版本。
    - `<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>`：指定了项目源代码文件的编码格式为 UTF-8，避免出现乱码。


**正确的注释应该只包含开头 `<!--` 和结尾 `-->`，中间不能出现连续的 `--`**

`<configuration>` 内部的子元素必须按照固定的顺序出现。正确的顺序是：  
`<properties>` → `<settings>` → `<typeAliases>` → ... → `<environments>` → `<databaseIdProvider>` → `<mappers>`。


mapper：````{<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dorm.mapper.UserMapper">
    <!-- 插入用户：useGeneratedKeys 可以获取自动生成的主键值 -->
    <insert id="insertUser" parameterType="com.dorm.entity.User" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO user (username, password, role)
        VALUES (#{username}, #{password}, #{role})
    </insert>
    <!-- 根据用户名查询用户 -->
    <select id="selectByUsername" parameterType="string" resultType="com.dorm.entity.User">
        SELECT * FROM user WHERE username = #{username}
    </select>
</mapper>

> **解释**：
> - `namespace` 必须和接口的全限定名一致（`com.dorm.mapper.UserMapper`）。  
> - `<insert>` 标签的 `id` 对应接口的方法名 `insertUser`。    
> - `parameterType` 是参数类型，`User` 类要写全限定名。   
>`#{username}` 是MyBatis的参数占位符，会自动从传入的User对象中取出 `username` 属性的值。  
> `<select>` 的 `resultType` 指定返回的结果要封装成什么类型的对象，这里返回一个 `User` 对象
}
````
