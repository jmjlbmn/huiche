# HuiChe [![GitHub issues](https://img.shields.io/travis/jmjlbmn/huiche.svg)](https://github.com/jmjlbmn/huiche/travis)
- 主要基于Spring boot
- 数据库连接池:druid (数据库连接池目前仅推荐使用druid或hikari)
- 数据查询 QueryDSL
- web容器 undertow

## 引入huiche-spring-boot-starter即可使用
具体可以参考[huiche-demo](https://github.com/jmjlbmn/huiche/tree/master/huiche-demo),目前只有简单搭建,后续完善


## sql-builder插件
目前仅支持MySql,依赖注解
- 通过Bean创建表,
- 通过数据表和Bean对比 更新数据表 

## codegen-querydsl插件
生成QueryDSL查询实体
