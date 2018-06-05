# HuiChe-Dao
HuiChe 数据库访问模块
- `crud` 常用增删改查操作
- `provider` 用于支持crud各操作共用数据
- `util` 用于数据访问的工具类
- `BaseDao` 基础Dao,所有的自定义Dao都应该继承他,用于自动注入sqlQueryFactory
- `BaseCrudDao` 通用的增删改查Dao,继承以实现了crud包里面所有接口的操作,集成了Java Bean Validation,创建和更新数据会自动验证(可关闭),大概能涵盖80%以上的单表查询需求(纯个人认为)
- `QueryDsl` 用于默认的QueryDsl配置和sql语句打印,一般不需要主动去使用