#说明
使用前,您应该熟悉maven和spring-boot
1. 配置pom文件parent为com.github.jmjlbmn:huiche(可选,但建议配置)
2. 增加依赖com.github.jmjlbmn:huiche-spring-boot-starter
3. 增加数据库依赖,如com.h2database:h2 或 mysql:mysql-connector-java
4. 在resource文件夹增加一个application.yml文件,然后配置
   - spring.datasource.url 您的jdbc-url
   - spring.datasource.username 您的数据库用户名
   - spring.datasource.password 您的数据库密码
5. 在java文件夹增加Application.java文件(名称随意,包名看自己)
   - 增加注解@SpringBootApplication
   - 增加main方法
   - main方法里面加入代码 SpringApplication.run(Application.class, args);
6. 执行main方法即可
