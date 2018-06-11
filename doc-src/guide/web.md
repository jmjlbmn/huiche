# 控制层
## Restful风格
`org.huiche.web.api.BaseCrudPageApi`, 提供默认的restful风格的Crud控制器接口
### CurdApi内置方法
uri|method|说明
:-|:-|:-
/{id}/|get|获取单条记录
/{id}/|delete|删除单条记录
/|post|根据是否设置id,创建或更新数据
/|get|分页获取数据
### CrudApi示例
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
## 传统Post风格
`org.huiche.web.controller.BaseCrudPageController`, 提供默认的post风格的Crud控制器接口
### PostController内置方法
uri|说明
:-|:-
/get/|获取单条记录
/save/|根据是否设置id,创建或更新数据
/del/|删除单条记录
/page/|分页获取数据
### CrudController示例
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