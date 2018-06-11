# 断言工具
`org.huiche.core.util.Assert`, 提供了业务逻辑常用的断言工具,不满足条件将抛出HuiCheException的异常,常用在service方法逻辑处理和控制器判断传入参数用
## 常用方法
方法名|说明
:-|:-
notNull|传入的多个参数有任何一个为空时抛出异常
ok|传入条件不是true时抛出异常
equals|传入的两个参数不相等时抛出异常
in|传入参数不在指定范围内时抛出异常
## 断言示例
```java
public class Demo{
    public void demo(){
       // 当用户名或密码有任一个为空时,抛出异常
       Assert.notNull("用户名和密码不能为空",userName,password);
       // 手机号码不正确时抛出异常
       Assert.ok("手机号码不正确",CheckUtil.isPhoneNumber(phone));
       // 用户锁定时不允许登录
       Assert.equals("用户被锁定,无法登录",LockingState.NotLocking,user.getLocking());
       // 订单未接单或处理中才可以取消
       Assert.in("订单不可以取消",order.getState(),State.WAIT,State.HANDLE); 
    }
}
```
## 异常处理
huiche的starter组件已经提供了全局的异常处理,会捕获HuiCheException统一返回BaseResult