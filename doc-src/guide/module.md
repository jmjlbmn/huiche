# 模块说明
## data 实体类
### 基础实体类
`org.huiche.data.BaseEntity`, 作为基础实体类, 它默认提供了如下属性
属性名|类型|说明
:-|:-|:-
id|Long|作为主键, 自增
createTime|String|创建时间(yyyy-MM-dd HH:mm:ss)
modifyTime|String|修改时间(yyyy-MM-dd HH:mm:ss)
### 实体类示例

```java
public class Student extend BaseEntity<Student>{
    private String name;
    private Integer sex;
}
```
::: tip 提醒
推荐使用 lombok 简化代码,@Accessors(chain = true)生成链式setter
:::

### 为什么...?
#### 为什么使用Long主键,而不类似SpringData提供主键泛型?
没什么特别理由,主要是为了方便封装增删改查,当您需要其他类型主键时,多添加一个属性当作实际使用的主键即可,huiche的定位即是中小项目的快速开发,当然您也可以fork进行调整
#### 为什么继承BaseEntity需要传递泛型?
主要是为了配合lombok,生成链式调用的setter方法时,设置BaseEntity提供的id,createTime,modifyTime可以安全的返回原类型


## dao 数据访问
### BaseDao
`org.huiche.dao.BaseDao`, 作为基础Dao, 它默认提供了SQLQueryFactory注入,用其来进行数据库操作
### BaseCrudDao
`org.huiche.dao.BaseCrudDao`, 作为单表查询的通用Dao,其提供了诸多方法及重载,方便进行单表数据查询
#### 内置数据操作方法
方法名|说明
:-|:-
create|创建单条记录
creates|批量创建多条记录
delete|删除数据
truncate|清空数据
update|更新单条记录
updates|批量更新多条记录
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

#### 内置扩展方法
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
### 通用CrudDao示例
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
### 关联查询示例
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
    public static final QBean<StudentScoreDTO> DTO = Projections.fields(
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

## service 业务逻辑
`org.huiche.service.BaseCrudServiceImpl`, 作为单表的通用CrudService,主要对BaseCrudDao进行了调用封装
### 内置方法
方法名|说明
:-|:-
dao|需要自行实现,返回对于的crudDao
create|创建数据
update|更新数据
save|根据id是否有值创建或更新
list|查询列表
page|分页查询
### CrudService示例
```java
public interface StudentService extends BaseCrudService{
}

@Service
public class StudentServiceImpl extends BaseCrudServiceImpl implements StudentService{
    @Resource
    private StudentDao studentDao;

    @Override
    protected BaseCrudDao<Student> dao() {
        return studentDao;
    }
}

```
## web 控制器
### Restful风格
`org.huiche.web.api.BaseCrudPageApi`, 提供默认的restful风格的Crud控制器接口
#### 内置方法
uri|method|说明
:-|:-|:-
/{id}/|get|获取单条记录
/{id}/|delete|删除单条记录
/|post|根据是否设置id,创建或更新数据
/|get|分页获取数据
#### CrudApi示例
```java
@RestController
@RequestMapping("api/student")
public class StudentApi extends BaseCrudPageApi {
    @Resource
    private StudentService studentService;

    @Override
    public BaseCrudService<Student> service(){
        return studentService;
    }
}
```
### 传统Post风格
`org.huiche.web.controller.BaseCrudPageController`, 提供默认的post风格的Crud控制器接口
#### 内置方法
uri|说明
:-|:-
/get/|获取单条记录
/save/|根据是否设置id,创建或更新数据
/del/|删除单条记录
/page/|分页获取数据
#### CrudController示例
```java
@RestController
@RequestMapping("student")
public class StudentController extends BaseCrudPageController {
    @Resource
    private StudentService studentService;

    @Override
    public BaseCrudService<Student> service(){
        return studentService;
    }
}
```
