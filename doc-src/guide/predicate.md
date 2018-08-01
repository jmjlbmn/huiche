# 条件拼装
`org.huiche.data.query.Query`,提供了方便查询的条件拼装的各种方法,默认在search,dao,service中已经被继承/实现,已方便调用
## 常用拼装方法
方法名|说明
:-|:-
predicate|构造一个条件
and|用`and`拼接多个条件为一个条件
predicates|等同`and`方法,用`and`拼接多个条件为一个条件
or|用`or`拼接多个条件
extendColumn|扩展列
excludeColumn|排除列

## 条件拼装示例
```java
    // 查询姓林的,身高>=180的男生或身高>=165的女生 的学生
    public class Demo{
        Predicate p = predicates(
                QStudent.student.name.startWith("林"),
                or(
                    predicates(
                        QStudent.student.sex.eq(Sex.MALE),
                        QStudent.student.height.goe(180)),
                    predicates(
                        QStudent.student.sex.eq(Sex.FEMALE),
                        QStudent.student.height.goe(165))));
    }
    
```