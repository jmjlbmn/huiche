# 业务层
`org.huiche.service.BaseCrudServiceImpl`, 作为单表的通用CrudService,主要对BaseCrudDao进行了调用封装
## service内置方法
方法名|说明
:-|:-
dao|需要自行实现,返回对于的crudDao
create|创建数据
update|更新数据
save|根据id是否有值创建或更新
list|查询列表
page|分页查询
## CrudService示例
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