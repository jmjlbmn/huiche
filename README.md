# HuiChe 
[![build](https://img.shields.io/travis/jmjlbmn/huiche.svg?style=flat-square)](https://travis-ci.org/jmjlbmn/huiche)
[![maven](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/github/jmjlbmn/huiche/maven-metadata.xml.svg?style=flat-square)](https://mvnrepository.com/artifact/com.github.jmjlbmn)
- 主要基于 Spring Boot
- 数据库交互 QueryDSL
- Web容器 Undertow
# 主要功能
- huiche-spring-boot-starter 一个依赖就能快速搭建项目
- 对dao层和service做了简单封装,包含基础的CRUD和常用查询
- [sql-builder](https://github.com/jmjlbmn/huiche-examples/tree/master/sql-builder)插件,通过实体类快速生成数据表,可单独使用
    > 避免依赖庞大的hibernate,支持修改字段和删除字段(默认只给出修改SQL语句,需手动执行)
- 数据库交互采用QueryDsl,快速,类型安全
- 通过数据库表生成QueryDsl查询实体 [codegen-querydsl](https://github.com/jmjlbmn/huiche-examples/tree/master/codegen-querydsl)
- 集成javax.validation验证,实体类增加注解,即可写入数据库之前自动进行验证


# 使用参考
示例: [huiche-examples](https://github.com/jmjlbmn/huiche-examples)