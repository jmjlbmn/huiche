# 数据访问层
## BaseDao
`org.huiche.dao.BaseDao`, 作为基础Dao, 它默认提供了SQLQueryFactory注入,用其来进行数据库操作
## BaseCrudDao
`org.huiche.dao.BaseCrudDao`, 作为单表查询的通用Dao,其提供了诸多方法及重载,方便进行单表数据查询
### 内置数据操作方法
方法名|说明
:-|:-
create|创建单条/多条记录
delete|删除数据
truncate|清空数据
update|更新记录
count|查询条数
exists|查询是否存在
get|返回一条记录的全部字段
getColumn|返回一条记录的某个字段
getColumns|返回一条记录的多个字段
list|返回多条记录的集合
listColumn|返回多条记录的某个字段的集合
listColumns|返回多条记录的某些字段的集合
page|分页获取指定记录的全部字段
pageColumns|分页获取指定记录的某些字段

### 内置扩展方法
方法名|说明|默认实现|建议重写场景
:-|:-|:-|:-
root|提供查询实体(数据表Path)|无|必须自行实现
pk|提供主键Path|Long id|不建议重写
beforeCreate|创建数据之前|设置创建时间更新时间|某些字段有默认值时
beforeUpdate|更新数据之前|设置更新时间|根据实际情况
validOnCreate|创建数据时进行验证(主要验证非空)|根据注解验证|不建议重写
validRegular|验证数据规则(长度,规则等)|根据注解验证|不建议重写
doValid|是否进行验证|进行验证|需要取消验证时
createSetId|创建数据时是否给id赋值|赋值|根据实际情况
defaultMultiOrder|默认多列排序|默认排序|需要改变默认排序且多列时
defaultOrder|默认排序|id倒序|需要改变默认排序时
## 通用CrudDao示例
```java
@Repository
public class StudentDao extends BaseCrudDao<Student> {
    @Nonnull
    @Override
    public RelationalPath<Student> root() {
        return QStudent.student;
    }
}
```
::: tip 提醒
- 其中Q开头的文件是QueryDsl生成的用于数据查询的'查询实体',
- 可以借助官方的maven插件自动生成
- 也可以使用`huiche-querydsl-codegen`插件手动生成(推荐)
:::
## 关联查询示例
查询指定小组的学生的成绩
```java
public class StudentScoreDTO{
    public String studentName;
    public Double score;
}
```
```java
@Repository
public class StudentDao extends BaseDao {
    public List<StudentScoreDTO> listStudentScore(long teamId) {
        return DataUtil.copyList(
            QueryUtil.list(
                sql()
                .select(
                    QStudent.student.name,
                    QStores.stores.store)
                .from(QStudent.student)
                .leftJoin(QStores.stores)
                .on(QStudent.student.id.eq(QStores.stores.studentId))
                .where(QStudent.student.teamId.eq(teamId))),
                item -> new StudentScoreDTO()
                    .setStudentName(item.get(QStudent.student.name))
                    .setScore(item.get(QStores.stores.store)));
    }
}
```
也可以
```java
@Repository
public class StudentDao extends BaseDao {
    // 可以将此常量直接定义到DTO里
    private static final QBean<StudentScoreDTO> DTO = Projections.fields(
        StudentScoreDTO.class, 
        QStudent.student.name.as("studentName"),
        QStores.stores.store);
    public List<StudentScoreDTO> listStudentScore(long teamId) {
        return QueryUtil.list(
                sql()
                .select(DTO)
                .from(QStudent.student)
                .leftJoin(QStore.store)
                .on(QStudent.student.id.eq(QStore.store.studentId))
                .where(QStudent.student.teamId.eq(teamId)));
    }
}
```