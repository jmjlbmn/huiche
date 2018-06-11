# 实体类
## 基础实体类
`org.huiche.data.BaseEntity`, 作为基础实体类, 它默认提供了如下属性
属性名|类型|说明
:-|:-|:-
id|Long|作为主键, 自增
createTime|String|创建时间(yyyy-MM-dd HH:mm:ss)
modifyTime|String|修改时间(yyyy-MM-dd HH:mm:ss)
## 实体类示例

```java
public class Student extends BaseEntity<Student>{
    private String name;
    private Integer sex;
}
```
::: tip 提醒
推荐使用 lombok 简化代码,@Accessors(chain = true)生成链式setter
:::

## 为什么...?
### 为什么使用Long主键,而不类似SpringData提供主键泛型?
没什么特别理由,主要是为了方便封装增删改查,当您需要其他类型主键时,多添加一个属性当作实际使用的主键即可,huiche的定位即是中小项目的快速开发,当然您也可以fork进行调整
### 为什么继承BaseEntity需要传递泛型?
主要是为了配合lombok,生成链式调用的setter方法时,设置BaseEntity提供的id,createTime,modifyTime可以安全的返回原类型
