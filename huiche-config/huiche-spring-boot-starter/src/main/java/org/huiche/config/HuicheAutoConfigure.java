package org.huiche.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import org.huiche.core.dao.QueryDsl;
import org.huiche.core.util.JsonUtil;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Maning
 */
@Configuration
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
public class HuicheAutoConfigure {
    @Bean
    public JsonUtil jsonUtil() {
        return new JsonUtil();
    }

    @Bean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource) {
        Provider<Connection> provider = new SpringConnectionProvider(dataSource);
        return new SQLQueryFactory(QueryDsl.CONFIG, provider);
    }
}
