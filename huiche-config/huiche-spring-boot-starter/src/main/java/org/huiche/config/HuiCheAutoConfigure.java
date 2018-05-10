package org.huiche.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.HibernateValidator;
import org.huiche.core.exception.BaseException;
import org.huiche.core.exception.SystemError;
import org.huiche.core.json.JsonApi;
import org.huiche.core.util.StringUtil;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

/**
 * HuiChe自动配置
 *
 * @author Maning
 */
@Configuration
@AutoConfigureOrder(Integer.MAX_VALUE)
public class HuiCheAutoConfigure {
    /**
     * 默认实现一个JsonApi
     *
     * @param objectMapper 注入jackson
     * @return jsonApi
     */
    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public JsonApi jsonUtil(ObjectMapper objectMapper) {
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
                    throw new BaseException(SystemError.JSON_ERROR);
                }
            }

            @Override
            @Nonnull
            public <T> T fromJson(@Nullable String json, @Nonnull Class<T> clazz) {
                try {
                    if(StringUtil.isEmpty(json)){
                        return clazz.newInstance();
                    }
                    return objectMapper.readValue(json, clazz);
                } catch (Exception e) {
                    throw new BaseException(SystemError.JSON_ERROR);
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
    @ConditionalOnMissingBean
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
    public Validator fastValidator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory().getValidator();
    }
}
