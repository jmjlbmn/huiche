# 验证
## 方法验证
huiche默认已经引入了JSR-305`com.google.code.findbugs:jsr305`,用其提供的注解,对方法的参数和返回值进行验证
- `javax.annotation.Nonnull`
- `javax.annotation.Nullable`
### 当添加在方法参数上时
@Nonnull 表示,该参数不允许传入null
@Nullable 表示,该参数可以传入null
### 当添加在方法上时
@Nonnull 表示,该方法不会返回null
@Nullable 表示,该方法可能返回null
### 为什么要这么做?
- 方便多人协作开发,减少不必要的重复判断,我的方法标注某参数@Nonnull,则应该由调用方法的地方进行判断,以满足调用方法的条件,当方法标注@Nonnull时,便不允许返回null,调用方法的用户也不必进行判断
- 为了配合IDE静态分析,尽可能的发现隐藏的bug
### 方法验证示例
```java
public class Demo{
    @Nonnull
    public String introduce(@Nonnull String name,@Nullable String hobby){
        if(null==hobby){
            return "我的名字是:" + name + ", 我没有特别喜欢的东西";
        }else{
            return "我的名字是:" + name + ", 我喜欢" + hobby;
        }
    }
}
```

## 数据验证
huiche默认引入了JSR-303/380`Bean Validation` 用于数据验证
> 默认仅在Dao层的`create/creates`和`updata/updates`方法中验证
### 数据验证示例
```java
public class Student extends BaseEntity<Student>{
    @NotEmpty(message = "姓名不能为空", groups = ValidOnlyCreate.class)
    @Length(message = "姓名不能超过6个字符", max = 6)
    private String name;
    @NotNull(message = "性别不能为空", groups = ValidOnlyCreate.class)
    private Integer sex;
}
```
将会进行如下验证
- CrudDao执行create时
  - 验证姓名`name`不能为空或空字符串,且长度不能超过6
  - 验证性别`sex`不能是null
- CrudDao执行update时
  - 验证姓名`name`长度不能超过6
您可能已经看出来,`ValidOnlyCreate`表示仅在create时验证
### 如何取消默认验证?
- 重写CrudDao的`doValid`方法,返回`false`(推荐)
- 重写`validOnCreate`和`validRegular`的实现
### 手动验证
内置了`ValidationUtil`可以注入使用,自行在合适的地方验证即可