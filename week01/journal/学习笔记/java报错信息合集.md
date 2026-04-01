1. C:\Users\36007>mvn -v The JAVA_HOME environment variable is not defined correctly, this environment variable is needed to run this program.
    这个错误是因为 Maven 需要 `JAVA_HOME` 环境变量来找到 JDK，但它当前没有指向一个有效的 JDK 安装路径。

2. org.apache.ibatis.exceptions.PersistenceException: ### Error building SqlSession. ### The error may exist in com/dorm/mapper/UserMapper.xml ### Cause: org.apache.ibatis.builder.BuilderException: Error parsing SQL Mapper Configuration. Cause: **java.io.IOException**: Could not find resource com/dorm/mapper/UserMapper.xml
    找不到文件UserMapper导致的ioException，先把这部分注释掉或者删掉

3. rg.apache.ibatis.exceptions.PersistenceException: ### Error building SqlSession. ### Cause: org.apache.ibatis.builder.BuilderException: Error creating document instance. Cause: **org.xml.sax.SAXParseException**; lineNumber: 23; columnNumber: 13; 注释中不允许出现字符串 "--"。
    修改xml注释内容

4. Exception in thread "main" org.apache.ibatis.exceptions.PersistenceException: ### Error updating database. Cause: **java.sql.SQLException**: Field 'name' doesn't have a default value ### The error may exist in com/dorm/mapper/UserMapper.xml ### The error may involve com.dorm.mapper.UserMapper.insertUser-Inline ### The error occurred while setting parameters ### SQL: INSERT INTO user (role, id, password) VALUES (?, ?, ?) ### Cause: java.sql.SQLException: Field 'name' doesn't have a default value
    -  `user` 表中有一个名为 `name` 的字段，它被设置为 `NOT NULL` 且没有默认值，但插入语句中没有包含这个字段，导致数据库拒绝插入。
    - 插入语句包含即可

5. Exception in thread "main" org.apache.ibatis.exceptions.PersistenceException: ### Error building SqlSession. ### The error may exist in com/dorm/mapper/UserMapper.xml ### Cause: **org.apache.ibatis.builder.BuilderException**: Error parsing SQL Mapper Configuration. Cause: org.apache.ibatis.builder.BuilderException: Error parsing Mapper XML. The XML location is 'com/dorm/mapper/UserMapper.xml'. Cause: org.apache.ibatis.builder.BuilderException: Parsing error was found in mapping #{name), #{password}. Check syntax #{property|(expression), var1=value1, var2=value2, ...}
    Parsing error was found in mapping #{name), #{password}
    错误原因：在 `UserMapper.xml` 中，写的占位符 `#{name)` 是错误的，应该用花括号 `}` 而不是圆括号 `)`。正确的写法是 `#{name}`。

6.  Exception in thread "main" **org.apache.ibatis.binding.BindingException**: Invalid bound statement (not found): com.dorm.mapper.UserMapper.selectById
    - 错误 `Invalid bound statement (not found): com.dorm.mapper.UserMapper.selectById` 说明 MyBatis 找不到 `selectById` 对应的 SQL 语句。你的插入操作成功了，证明 `UserMapper.xml` 的 `namespace` 是正确的，但缺少 `selectById` 的映射
    - mapper.xml里添加<select>标签


7. Exception in thread "main" **org.apache.ibatis.exceptions.PersistenceException**: ### Error updating database. Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '3225004433' for key 'user.PRIMARY' ### The error may exist in com/dorm/mapper/UserMapper.xml ### The error may involve com.dorm.mapper.UserMapper.insertUser-Inline ### The error occurred while setting parameters ### SQL: INSERT INTO user (role, id, name, password) VALUES (?, ?, ?, ?) ### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '3225004433' for key 'user.PRIMARY'
    - 主键重复，因为表中已经存在 `id='3225004433'` 的记录。测试代码再次插入同样的主键，导致违反唯一约束。

8. Exception in thread "main" **org.apache.ibatis.exceptions.PersistenceException**: ### Error querying database. Cause: **java.sql.SQLSyntaxErrorException**: Unknown column 'orderId' in 'order clause' ### The error may exist in com/dorm/mapper/UserMapper.xml ### The error may involve com.dorm.mapper.UserMapper.selectById-Inline ### The error occurred while setting parameters ### SQL: SELECT * FROM user WHERE id = ?ORDER BY orderId ### Cause: java.sql.SQLSyntaxErrorException: Unknown column 'orderId' in 'order clause'

9. Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
Reason: Failed to determine a suitable driver class
- 这表示 Spring Boot 没有找到数据库连接的配置（URL、用户名、密码）。在项目中添加 `application.yml` 文件，告诉 Spring Boot 如何连接 MySQL 数据库。

 10. org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'repairorderService': Unsatisfied dependency expressed through field 'repairorderMapper': Error creating bean with name 'repairorderMapper' defined in file [D:\Users\starter\demo\target\classes\com\example\demo\mapper\RepairorderMapper.class]: Unsatisfied dependency expressed through bean property 'sqlSessionFactory': Error creating bean with name 'sqlSessionFactory' defined in class path resource
- `application.yaml` 缩进有误，导致 `datasource` 被放在了 `application` 节点下，而不是 `spring` 的直接子节点。Spring Boot 因此找不到数据源配置。
  `datasource` 必须放在 `spring` 节点下，而不是与 `spring` 平级。



