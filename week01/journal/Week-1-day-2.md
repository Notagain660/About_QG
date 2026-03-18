# XML
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
        <property name="url" value="jdbc:mysql://localhost:3306/dorm_repair?      useSSL=false&amp;serverTimezone=UTC"/>
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



# Git&GitHub
**配置身份**:
- `git config --global user.name "Github 名称"`
- `git config --global user.email "github mail"`
**初始化本地仓库**
- 如`cd "D:/Users/starter······"`
- `git init`
**添加文件和提交**
- `git add .`，add后面加目标文件所在目录是部分上传，这个是全部上传
- `git commit -m "first commit"`像这样，双引号里是提交的描述
**关联远程仓库并提交**
- GitHub新建空白仓库
- ```{
   git remote add origin https://github.com/你的用户名/仓库名.git
   git branch -M main
   git push -u origin main//第一次，后面直接git push就行
   }
  ```

**配置网络环境**
- `git config --global https.proxy http://127.0.0.1:端口地址`
- `git config --global --unset https.proxy`
- `curl -x http://127.0.0.1:端口地址 https://github.com -I`测试代理是否工作
**修改根目录**
在想要换成的根目录下初始化本地新仓库，关联想换根目录的远程仓库，强制推送（会丢失原远程仓库里面的数据）
示例：`git branch -M main`
`git push -u origin main --force`
**删除远程仓库**
`git remote rm origin        # 删除旧的`
`git remote add origin 新URL # 添加新的`


# MyBatis
> “mybatis起到一个作为数据库MySQL和dorm这个层级的接口的作用（？）”

 MyBatis 是一个**ORM（对象关系映射）框架**，它的作用就是：

- 让你用Java对象（如`User`）操作数据库表（如`user`），不用写繁琐的JDBC代码。
- 你只需要定义Mapper接口（比如 `UserMapper.java`），然后在XML里写SQL语句，MyBatis就会自动实现接口方法，执行SQL并返回结果。

**类比C语言：**  
相当于你写了一个库，它帮你把数据库操作封装成函数，你只需要调用 `insertUser(user)`，它自动处理连接、语句、结果集。你不再需要手动 `fopen`、`fwrite`、`fclose`。


# Maven
> “maven相当于帮你把要用的外部库统统一把梭了，你只需要在项目配置maven（？）”

Maven 是一个**项目管理和构建工具**，核心功能：

- **依赖管理**：你在 `pom.xml` 里写 `<dependency>`，Maven自动从中央仓库下载对应的jar包（如MySQL驱动、MyBatis），并加入到项目类路径中。不用手动下载和复制。
- **项目构建**：编译、打包、运行测试等。
    
**类比C语言：**

- 传统C项目：你需要手动下载第三方库（比如 `libmysql`），把 `.h` 和 `.a/.so` 文件放到项目里，然后在编译时指定路径。
- Maven相当于一个**包管理器**（像Linux的 `apt`），加上一个**自动化构建工具**（像 `make` 但更强大）。你只需声明依赖，它全自动处理。
**外部库用来干嘛？**

- `mysql-connector-j`：让Java能连接MySQL数据库（相当于C语言的MySQL C API库）。
- `mybatis`：让你能用更简单的方式操作数据库（相当于一个封装好的数据库操作框架）。

C语言里需要自己找对应的库，然后处理链接。Java不用。

# MySQL

