# 开始使用
## 说明
[![build](https://img.shields.io/travis/jmjlbmn/huiche.svg?style=flat-square)](https://travis-ci.org/jmjlbmn/huiche)
[![maven](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/org/huiche/huiche/maven-metadata.xml.svg?style=flat-square)](http://search.maven.org/#artifactdetails%7Corg.huiche%7Chuiche%7C1.1.0%7C)
- 基于QueryDsl的快速,类型安全的SQL查询
- 涵盖大部分单表查询场景的通用Dao
- 方便实用的关联查询
- 基于java.util.function编写的函数式编程方式的动态数据筛选功能
- 基于jsr-303/380(Bean Validation)的注解式验证
- 基于jsr-305的静态分析
- 提供restful和传统单post两种方式的常用增删改查功能的BaseController
- huiche-spring-boot-starter快速搭建项目
- 基于实体类注解的Sql建表支持,可独立框架外使用,支持修改字段和删除字段,详细参考: [sql-builder](https://github.com/jmjlbmn/huiche-examples/tree/master/sql-builder)
## 快速开始
一、parent方式(推荐)
```xml
<project>
    <parent>
        <groupId>org.huiche</groupId>
        <artifactId>huiche</artifactId>
        <version>1.1.0</version>
    </parent>
    <dependencies>
        <dependency>
            <groupId>org.huiche</groupId>
            <artifactId>huiche-spring-boot-starter</artifactId>
            <version>1.1.0</version>
        </dependency>
    </dependencies>
</project>
```
二、dependencyManagement方式
```xml
<project>
    <properties>
        <!--使用此方式,需要额外指定java版本(>=8)-->
        <java.version>1.8</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
    </properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.huiche</groupId>
                <artifactId>huiche</artifactId>
                <version>1.1.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.huiche</groupId>
            <artifactId>huiche-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
</project>