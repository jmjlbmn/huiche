# Sql Builder
根据实体类和注解,生成 创建/更新 数据表的SQL语句,支持Boolean,Integer,Long,Float,Double,String,枚举(按String,存名称)
## 实体类添加注解,需要添加依赖(就两个类,直接复制过去用也是可以的啦,需保持包名类名不能变)
```xml
    <!--版本请自行添加或使用dependencyManagement-->
    <dependencies>
        <dependency>
            <groupId>org.huiche</groupId>
            <artifactId>huiche-annotation</artifactId>
        </dependency>
    </dependencies>
```

## 实体类/表注解 `org.huiche.annotation.sql.Table` 参数:
- `value` 表名,不填默认值为类名用下划线分隔,小写的形式,如实体UserGroup,默认表名为user_group
- `comment` 表注释
> `Table` 注解必须添加,不然不会被扫描到
## 属性/字段注解 `org.huiche.annotation.sql.Column` 参数:
- `value` 字段名,不填默认值为类名用下划线分隔,小写的形式,如字段userRole,默认字段名为user_role
- `isPrimaryKey` 是否主键,默认`false`
- `isAutoIncrement` 是否自增,默认true,仅字段是主键时生效
- `length` 字段长度,仅适用于字符类型和小数类型(DECIMAL)的字符长度,
    > 在Mysql中,length设置4000(`Mysql.Length.TEXT`)以上为TEXT类型,60000(`Mysql.Length.LONGTEXT`)以上为LONGTEXT类型
- `precision` 精度,仅适用于小数类型的精度,即小数位数
- `unique` 是否是唯一,默认`false` 设为true会创建唯一索引
    > 注意:目前仅支持新增字段时的唯一索引创建,如果变更unique您需要手动去管理索引
- `notNull` 是否非空,默认`false`
- `isDbField` 是否是数据表字段,默认`true` 设置为`false`时将跳过该字段
- `comment` 列注释
> `Column` 注解可选添加,不添加时默认当做字段并自动解析出字段名和长度,其他使用默认值
## 执行生成所需依赖(推荐放在单独的,不会被打包的模块,按需手动执行,因为项目运行并不需要它)
```xml
    <!--版本请自行添加或使用dependencyManagement-->
    <dependencies>
        <!--数据库驱动,目前仅支持MySQl,可自行扩展-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.huiche</groupId>
            <artifactId>huiche-sql-builder</artifactId>
        </dependency>
    </dependencies>
```
## 初始化和执行
```java
    public class CreateTable {
        public static void main(String[] args) {
            SqlBuilder builder = SqlBuilder.init(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD);
            builder.run();
        }
    }
```
#### 其中`init`方法可选设置属性:
- `rootPath` 扫描根路径,默认为项目根路径,如果不准确或者需要精确时可以指定
- `namingRule` 默认命名规则,会优先使用注解里面的value值,如果未设置才会使用namingRule
- `sql` 手动指定Sql接口的实现,仅用于需要扩展其他数据库或者重写方法的时候
    > 手动指定sql实现时,需要自行先行注册驱动`Class.forName(YourDriver.class)`
#### 其中`run`方法可选设置属性:
- `update` 是否执行修改字段和删除字段操作,默认`false`不执行,但会在控制台输出需要执行的SQL语句,您可手动执行,设置`true` 会一并执行修改字段和删除字段操作
- `Class<?>... classes` 可选填写0~N个实体类,填写时,仅会处理填写的实体类,不填默认扫描加载到ClassPath的全部有@Table注解的类
- `String... packageName` 可选填写-~N个包名,填写时,仅会处理填写的包名下的类,不填默认扫描加载到ClassPath的全部有@Table注解的类