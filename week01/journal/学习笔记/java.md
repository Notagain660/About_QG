主要参考文献：廖雪峰的JAVA文档
其它参考内容：jdk21官方文档，deepseek

## 基本事实
JAVA里的变量更像是指向存在某个内存地址的值，变更值后指向存在新的内存地址的值。
没有任何被引用的对象的内存会被回收，不回收可能内存泄漏，如：
1. 把对象放到一个静态集合（如 List、Map）中，用完后忘记移除。
2. 内部类持有外部类的隐式引用。
基本类型值直接存储在栈内存

## 基本语法
### switch
1. case 1 -> {}（自动break）（jdk12新增）
2. switch里return语句退出本次循环
3. yield返回值（jdk14）。
### for each循环（jdk12新增）
e.g: String str ＝ {"番茄", "土豆", "黄瓜"}; for(String s ; str){//可以遍历str里面的每一个String类型元素
}


### 输出（System.out类）
1. printf类似c的printf（%[参数在参数列表的位置$][输出格式，如tm，te][数据宽度[正右对齐负左对齐][精度][数据类型标识符]）
2. print输出数字
3. println输出且换行没有标识符。

### 输入
1. java.util.Scanner类，创建对象并传入System.in类然后引用
2. scanner.nextLine可以吸收换行符（类比c的getline），不同数据类型方法不同。


引用类型（比如字符串和instance）比较并且得到boolean结果用equal（但是空指针不行否则NullPointerException）

main 方法接受命令行参数（String[]，jvm接收用户输入并传给string）


### 基本类型
Long和long（基元类型）：

字符串可以加号连接

逻辑运算符短路（算到绝对true/false就不算后面的）


### 打印
目录/选项：list遍历或者多行字符串（格式容易乱，jdk13新增）
一维数组：Arrays.toString(arrayname)（倒序打印要访问数组索引）
二维：deepToString(arrayname)
排序：Array.sort(arrayname)（变化指向）


### Exception
不建议返回空指针可能触发NullPointerException，空对象或者空字符串OK。


### 面向对象
**面向过程**：从已知条件出发分流程通过方法（函数）找结果

**面向对象**：先有结果对应的对象再判断需要采取的方法然后写方法去接收/连接已知条件（？）

**调用方法**
- 导入类new instance（实例），实例变量.方法/类.方法（有前提！！）
- 方法可以写对外接口（比如mapper）
- this：访问（可以理解为载入？）当前实例的字段（防止重名）

**方法签名**
- 修饰符+方法名+方法参数列表
- 可变参数相当于数组类型（但是不容易判断非法输入），可以防止NullPointerException
- 参数绑定：参数修改前调用这个方法，方法接收到的值是参数当前值的复制，所以参数修改不会改变调用方接收的值。
- 引用类型参数绑定只要指向的是同一个对象（比如同一个数组）就会一起变。

**类**
- class，定义实例创建模板，字段：类的特征（用到的量）
- 构造方法：new对象实例时就自动调用把内部字段初始化（通过传入的参数）（默认构造方法：空）（可以构造方法调用构造方法，this(参数列表)同名也没事
- 任何class的构造方法，第一行语句必须是调用父类的构造方法。（super()）

**修饰**
1. public（可以随意访问）
2. private（拒绝外界访问，外界方法可以间接访问，private可以访问private）
3. protected（仅子类可访问继承）
4. final：固定值（字段）或者禁止override的方法和禁止继承的类


### overload&override
- **overload**：（方法签名不同，返回值相同））

- **override**：方法签名相同，返回值类型相同，内部逻辑不同


### 继承
- 类A，类B在类Apublic/protected字段/方法基础上多了其它字段和方法，类B可以继承自类A（但是最好完全是从属（is）关系的，衍生不行否则逻辑混乱）

- e.g:类A有子类B，C，new类A对象实例时可以：
````
{[ ] instancename＝ new A[ ]{
new B（参数）
new C（参数）
}}
````

- 最终根为object
- (对对象操作）向上转型（实例）安全；
- 向下转型instanceof判断（判断变量指向的对象实例是不是指定类型（的子类）），jdk14把判断和判断正确之后的定义和在一起
- **组合**：has关系，比如userservice里面protected User user

- **多态**：调用时取决于当时的实例是什么，不需要知道到底所属哪个类。（父类子类关系）
当一个具体的`class`去实现一个`interface`时，需要使用`implements`关键字，一个类可以实现多个`interface`
本项目不用implement的原因：MyBatis 在运行时，会**自动生成一个该接口的实现类（代理对象）**，这个代理对象会：
1. 读取 XML 或注解中的 SQL 语句。
2. 当调用 `selectById` 方法时，代理对象会执行对应的 SQL，并返回结果。
这个动态生成的实现类，就是 MyBatis 帮你写的“具体实现”。它使用了 Java 的**动态代理**技术，在内存中动态创建了一个实现了 `UserMapper` 接口的类，并接管了所有方法调用。
**所以，不需要手动写 `implements UserMapper`，因为 MyBatis 在运行时做了。**

## 常见类
- Object.equals可以用在可能为空的字符串比较，deepequals主要用于数组（尤其二维）在字符串会退化成为equals



### 注解
 `@Nullable` 和 `@NotNull` 是 IntelliJ IDEA **自动推断**出的注解，用于辅助代码分析，**不影响程序运行**，但能帮助在编码时发现潜在的空指针问题。
- `@Nullable` 表示方法返回值可能为 `null`（比如登录失败时），提醒调用者处理。
- `@NotNull` 表示参数不应该传入 `null`，如果传入 `null`，IDEA 会给出警告。



### 记录类（record）
- 一种特殊的类，用 `record` 关键字声明。
- 编译器会自动生成：
    - 一个全参构造器（参数顺序与字段声明一致）
    - 每个字段的 `private final` 成员变量
    - 公开的 `getter` 方法（方法名与字段名相同，例如 `success()` 而不是 `getSuccess()`）
    - `equals()`、`hashCode()`、`toString()` 方法
- 字段默认是 `final` 的，所以记录类是不可变的（immutable）
- 记录类不能扩展其他类（但可以实现接口）。
- 如果需要自定义构造器或方法，可以添加静态工厂方法 `success()` 和 `fail()`。
- 获取字段值时，使用 `result.success()` 而不是 `result.getSuccess()`（但你的 `Main` 中还在用 `isSuccess()`，需要改为 `success()`

@param prompt 提示信息
@param validator 校验规则（如 n -> n >= 1 && n <= 7）











