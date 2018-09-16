# 控制层
`org.huiche.web.api.BaseCrudPageApi`, 提供默认的restful风格的Crud控制器接口
## CurdApi内置方法
uri|method|说明
:-|:-|:-
/{id}/|get|获取单条记录
/{id}/|delete|删除单条记录
/|post|创建数据
/{id}|patch|更新数据(部分更新)
/|get|分页获取数据
## CrudApi示例
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