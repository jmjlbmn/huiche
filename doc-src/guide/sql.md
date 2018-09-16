# 建表工具
huiche提供了一个`huiche-sql-builder`插件,可以根据实体类建表(生成建表sql并执行),支持修改
## 依赖注解
- `org.huiche.annotation.sql.Table` 标注实体类,进行表的描述
- `org.huiche.annotation.sql.Column` 标注属性,进行字段的描述
## @Table注解
> 必要,会依赖这个注解来扫描

属性|说明|默认值|已存在时是否更新
:-|:-|:-|:-
value|表名|小写字母和_拼接|<span style="color:#f00">不更新,变更时会认为是新表,但不对旧表进行删除</span>
comment|注释|无|进行更新
charset|编码|mysql时为`utf8mb4`|<span style="color:#f00">不进行更新,仅创建时设置</span>
engine|引擎|mysql时为`InnoDB`|<span style="color:#f00">不进行更新,仅创建时设置</span>
collation|排序|无,按数据库默认规则|<span style="color:#f00">不进行更新,仅创建时设置</span>
## @Column注解
> 可选,但建议都提供注释`comment`,<b>目前主键字段不会进行任何更新/变动</b>

属性|说明|默认值|已存在时是否更新
:-|:-|:-|:-
value|字段名|小写字母和_拼接|<span style="color:#f00">不更新,变更时会认为是新字段,且删除旧字段(默认打印删除sql但不自动执行)</span>
isPrimaryKey|是否主键|false|不更新(原主键除备注和长度外其他改动都不更新)
isAutoIncrement|是否自增,仅字段时主键时生效|是主键时默认true,其他false|不更新(原主键除备注和长度外其他改动都不更新)
length|字段长度,仅varchar类型生效|255|更新
precision|字段的精度,仅小数类型生效|0|更新
unique|字段是否是唯一|false|不更新(删除索引不安全,所以干脆不进行更新)
notNull|字段是否允许是null|false|更新
isDbField|是否时数据库字段,false时不会创建该字段|true|<span style="color:#f00">更新,改为false会删除字段(默认打印删除sql但不自动执行)</span>
comment|字段注释/说明|无|更新

## 类型支持及映射
java类型|JDBCType|长度(@Column#length)|mysql类型
:-|:-|:-|:-
Boolean|TINYINT||tinyint
Integer|INTEGER||int
Long|BIGINT||bigint
Float|DOUBLE||double
Double|DOUBLE||double
BigDecimal|DECIMAL||decimal
String|VARCHAR|<4000|varchar
String|VARCHAR|>=4000 && <60000|text
String|VARCHAR|>=60000|longtext
## 建表工具类
`org.huiche.extra.sql.builder.SqlBuilder` 需要先进行`init`方法初始化,然后执行`run`方法即可
- init至少需要提供jdbc的url,user,password
- run有多个重载,最常用为无参或传递一个boolean参数,标注是否进行字段修改/删除等危险操作
> `run`传递true时会自动执行相关SQL,传递false或直接调用无参`run`,则不会自动执行相关语句,但是会生成语句打印在控制台,可以手动执行

## 实体类注解示例
```java
@Table(comment="学生信息表")
public class Student extends BaseEntity<Student>{
    @Column(comment="姓名",length=10)
    private String name;
    @Columen(comment="性别")
    private Integer sex;
}
```
## 建表方法示例
```java
public class CreateTable {
    public static void main(String[] args) {
        SqlBuilder builder = SqlBuilder.init(JDBC_URL, DB_USER, DB_PASSWORD);
        // 生成建表语句并执行,但不会执行字段修改或删除等危险SQL
        // 会在控制台进行打印,可以根据实际情况手动执行
        builder.run("packageName");// 等同builder.run(false,"packageName");
        // 生成建表语句并执行,包括字段修改和删除等危险SQL,(建议仅开发初期使用)
        builder.run(true,"packageName");
    }
} 
```
