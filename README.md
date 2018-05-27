# HuiChe
[![build](https://img.shields.io/travis/jmjlbmn/huiche.svg?style=flat-square)](https://travis-ci.org/jmjlbmn/huiche)
[![maven](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/org/huiche/huiche/maven-metadata.xml.svg?style=flat-square)](http://search.maven.org/#artifactdetails%7Corg.huiche%7Chuiche%7C1.0.1%7C)
## 基于SpringBoot和QueryDsl的快速开发框架
## 主要功能
- 基于QueryDsl的快速,类型安全的SQL查询
- 涵盖大部分单表查询场景的通用Dao
- 方便实用的关联查询
- 基于java.util.function编写的函数式编程方式的动态数据筛选功能
- 基于jsr-303(Bean Validation)的注解式验证
- 基于jsr-305的静态分析
- 提供restful和传统单post两种方式的常用增删改查功能的BaseController
- huiche-spring-boot-starter快速搭建项目
- 基于实体类注解的Sql建表支持,可独立框架外使用,支持修改字段和删除字段,详细参考: [sql-builder](https://github.com/jmjlbmn/huiche-examples/tree/master/sql-builder)
## 使用参考
- [huiche-examples](https://github.com/jmjlbmn/huiche-examples)

## Maven 当前版本为: 1.0.1
一、配置为pom文件parent(推荐)
```xml
<parent>
    <groupId>org.huiche</groupId>
    <artifactId>huiche</artifactId>
    <version>1.0.1</version>
</parent>
```
或,在dependencyManagement开始的位置添加
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.huiche</groupId>
            <artifactId>huiche</artifactId>
            <version>1.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
二、添加starter依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.huiche</groupId>
        <artifactId>huiche-spring-boot-starter</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```
如果想要体验最新功能或需要较快的bug修复,可以使用SNAPSHOT版本,版本号一般是:
- 正式版本:`x.y.z` 如`1.0.0`
- SNAPSHOT版本:`x.y.(z+1)-SNAPSHOT` 如`1.0.1-SNAPSHOT`
- 另外需要额外在pom文件配置SNAPSHOT版本的仓库

```xml
<repositories>
  <repository>
      <id>oss</id>
      <name>oss snapshots</name>
      <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
      <releases>
          <enabled>false</enabled>
      </releases>
      <snapshots>
          <enabled>true</enabled>
      </snapshots>
  </repository>          
</repositories>
```  