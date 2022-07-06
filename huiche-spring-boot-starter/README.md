# HuiChe-Spring-Boot-Starter
HuiChe Starter 快速搭建HuiChe框架(基于Spring Boot),自动进行如下操作
- 选择Druid当作数据库连接池
- 选择Undertow替代Tomcat作为Web容器
- 创建默认的SqlQueryFactory(用于基于QueryDsl的数据库查询)
- 实现了基于Jackson的json转换接口 `org.huiche.core.json.JsonApi`,可直接注入JsonApi使用
- 默认进行了全局异常处理,统一返回了`BaseResult`