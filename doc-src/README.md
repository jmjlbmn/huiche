---
home: true
heroImage: /huiche.png
actionText: 快速上手 →
actionLink: /guide/
features:
- title: SpringBoot 驱动
  details: 基于 Spring Boot, 同时提供 starter 组件, 默认集成Druid连接池并配置Undertow作为Web容器,并提供轻量级的建表工具,尽可能的免配置, 快速搭建。
- title: QueryDsl 数据交互
  details: 提供方便快捷,类型安全的SQL查询,提供通用的Crud方法,覆盖大部分单表查询场景,不必额外写Dao,只需要关注和编写业务相关的需要关联查询的Dao。
- title: 基于 Java 8
  details: 设计伊始便基于Java8,以快速开发为目的,希望借助其function,stream,接口的默认与静态方法及lambda表达式等新特性提供方便,简洁,快速的开发体验
footer: Apache Licensed | Copyright © 2018-present Jmjlbmn
---
[![build](https://img.shields.io/travis/jmjlbmn/huiche.svg?style=flat-square)](https://travis-ci.org/jmjlbmn/huiche)
[![maven](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/org/huiche/huiche/maven-metadata.xml.svg?style=flat-square)](http://search.maven.org/#artifactdetails%7Corg.huiche%7Chuiche%7C1.0.2%7C)
```xml
<project>
    <parent>
        <groupId>org.huiche</groupId>
        <artifactId>huiche</artifactId>
        <version>RELEASE</version>
    </parent>
    <dependencies>
        <dependency>
            <groupId>org.huiche</groupId>
            <artifactId>huiche-spring-boot-starter</artifactId>
            <version>RELEASE</version>
        </dependency>
    </dependencies>
</project>
```