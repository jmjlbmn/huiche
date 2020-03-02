package org.huiche.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.SQLBaseListener;
import com.querydsl.sql.SQLListenerContext;
import com.querydsl.sql.SQLQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.huiche.core.exception.HuiCheException;
import org.huiche.core.json.JsonApi;
import org.huiche.core.util.StringUtil;
import org.huiche.core.validation.ValidationUtil;
import org.huiche.dao.MySqlExTemplates;
import org.huiche.dao.QueryDsl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HuiChe自动配置
 *
 * @author Maning
 */
@Configuration
@Slf4j
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
                        return clazz.getConstructor().newInstance();
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
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource) {
        Provider<Connection> provider = new QueryDslConnectionProvider(dataSource);
        QueryDsl.init(new MySqlExTemplates());
        QueryDsl.CONFIG.addListener(new SQLBaseListener() {
            @Override
            public void end(SQLListenerContext context) {
                Connection connection = context.getConnection();
                if (connection != null && context.getData(PARENT_CONTEXT) == null) {
                    DataSourceUtils.releaseConnection(connection, dataSource);
                }
                super.end(context);
            }
        });
        return new SQLQueryFactory(QueryDsl.CONFIG, provider);
    }

    private static final String PARENT_CONTEXT = AbstractSQLQuery.class.getName() + "#PARENT_CONTEXT";

    /**
     * 注册验证工具
     *
     * @param validator 验证器
     * @return 验证工具
     */
    @Bean
    @ConditionalOnMissingBean
    public ValidationUtil validationUtil(@Qualifier("fastValidator") Validator validator) {
        return objects -> {
            List<String> list = new ArrayList<>();
            for (Object obj : objects) {
                if (null != obj) {
                    list.addAll(validator.validate(obj).stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
                }
            }
            return list;
        };
    }
}
