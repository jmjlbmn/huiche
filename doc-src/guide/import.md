# 数据迁移工具
huiche基于QuerDsl提供了一个数据迁移工具`DataTransfer`,它可能没有多么的`智能`和`快速`,但可以方便的进行新老数据的转换,所以他的优点在于`灵活`(当新旧的数据结构没有什么改变或改变很少时,建议还是由其他数据导入工具进行快速导入),原理就是简单的把原有数据查询出来,然后根据mapper(自行实现),转换为新的数据然后批量插入.
## 数据迁移构造
现在默认提供3种方式构造,需要分别提供source源数据表和target目标数据库,(理论上支持大部分主流数据库,但目前仅对MySql进行了大量测试)
构造参数|说明
:-|:-
SqlQueryFactory|QueryDsl依赖的sql查询工厂
DataSource|数据源连接池
url/user/pwd| jdbc连接的url,用户名,密码
## 数据迁移
构造`DataTransfer`之后,即可执行`transfer`进行数据迁移,它提供了多个重载以方便使用,下面仅对全部参数的方法进行介绍
```java
public class DataTransfer{
/**
     * @param srcTablePath      源表 path
     * @param srcQueryPredicate 源表过滤条件
     * @param targetTablePath   目标表 path
     * @param mapper            数据转换mapper
     * @param size              每次处理多少条数据
     * @param <S>               源类型
     * @param <T>               目标类型
     */
    public <S, T> void transfer(
        RelationalPath<S> srcTablePath, 
        Predicate srcQueryPredicate, 
        RelationalPath<T> targetTablePath, 
        Function<S, T> mapper, 
        int size) {
    }
}
```