package org.huiche.config;

import org.huiche.core.util.JsonUtil;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Maning
 */
@Configuration
@AutoConfigureOrder(Integer.MAX_VALUE)
public class HuiCheBaseAutoConfigure {
    @Bean
    @ConditionalOnMissingBean
    public JsonUtil jsonUtil() {
        return new JsonUtil();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication
    public ErrorHandler errorHandler() {
        return new ErrorHandler();
    }
}
