# HuiChe Demo
开始之前,您应该熟悉maven和spring-boot
1. 配置pom文件parent为com.github.jmjlbmn:huiche(可选,但建议配置)
    ```xml
        <parent>
            <groupId>com.github.jmjlbmn</groupId>
            <artifactId>huiche</artifactId>
            <version>版本</version>
        </parent>
    ```
2. 增加依赖com.github.jmjlbmn:huiche-spring-boot-starter
    ```xml
        <dependencies>
            <dependency>
                <groupId>com.github.jmjlbmn</groupId>
                <artifactId>huiche-spring-boot-starter</artifactId>
            </dependency>
            <!--其他依赖-->
        </dependencies>
    ```
3. 增加数据库依赖,如com.h2database:h2 或 mysql:mysql-connector-java
    ```xml
        <dependencies>
            <!--其他依赖-->
            <!--数据库依赖-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
            </dependency>
        </dependencies>
    ```
4. 在resource文件夹增加一个application.yml文件,然后配置
    ```yaml
        spring:
          datasource:
            url: jdbc-url #如 jdbc:mysql://localhost:3306/DB_Name?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&autoReconnectForPools=true&useSSL=true&createDatabaseIfNotExist=true
            username: 用户名
            password: 密码
    ```
5. 在java文件夹增加Application.java文件(名称随意,包名看自己)
    ```java
        @SpringBootApplication
        public class Application {
            public static void main(String[] args) {
                SpringApplication.run(Application.class, args);
            }
        }
    ```
6. 执行main方法即可
