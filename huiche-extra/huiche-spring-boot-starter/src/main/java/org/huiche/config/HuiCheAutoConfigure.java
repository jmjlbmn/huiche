package org.huiche.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import org.hibernate.validator.HibernateValidator;
import org.huiche.core.exception.HuiCheException;
import org.huiche.core.json.JsonApi;
import org.huiche.core.util.StringUtil;
import org.huiche.dao.MySqlExTemplates;
import org.huiche.dao.QueryDsl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import java.sql.Connection;

/**
 * HuiChe自动配置
 *
 * @author Maning
 */
@Configuration
@AutoConfigureAfter({DataSourceAutoConfiguration.class, JacksonAutoConfiguration.class})
public class HuiCheAutoConfigure {
    /**
     * 默认实现一个JsonApi
     *
     * @param objectMapper 注入jackson
     * @return jsonApi
     */
    @Bean
    @ConditionalOnBean({ObjectMapper.class, JacksonAutoConfiguration.class})
    @ConditionalOnMissingBean
    public JsonApi jsonApi(ObjectMapper objectMapper) {
        return new JsonApi() {
            @Override
            @Nonnull
            public String toJson(@Nullable Object object) {
                try {
                    if (null == object) {
                        return "";
                    }
                    return objectMapper.writeValueAsString(object);
                } catch (JsonProcessingException e) {
                    throw new HuiCheException(e);
                }
            }

            @Override
            @Nonnull
            public <T> T fromJson(@Nullable String json, @Nonnull Class<T> clazz) {
                try {
                    if (StringUtil.isEmpty(json)) {
                        return clazz.newInstance();
                    }
                    return objectMapper.readValue(json, clazz);
                } catch (Exception e) {
                    throw new HuiCheException(e);
                }
            }
        };
    }

    /**
     * 注册全局异常处理
     *
     * @return 异常处理
     */
    @Bean
    @ConditionalOnWebApplication
    public ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    /**
     * 注册dao的快速验证器
     *
     * @return 验证器
     */
    @Bean("fastValidator")
    @ConditionalOnClass(HibernateValidator.class)
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory().getValidator();
    }

    /**
     * 注册SQLQueryFactory,默认MySql,如用其他db请注册SQLTemplates的bean 或自行注册SQLQueryFactory
     *
     * @param dataSource 数据源
     * @return SQLQueryFactory
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource) {
        Provider<Connection> provider = new SpringConnectionProvider(dataSource);
        QueryDsl.init(new MySqlExTemplates());
        return new SQLQueryFactory(QueryDsl.CONFIG, provider);
    }
}
