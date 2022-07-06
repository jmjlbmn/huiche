package org.huiche.autoconfigure;

import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.types.EnumByNameType;
import org.huiche.support.NamingUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;

/**
 * @author Maning
 */
public class QuerydslMapperScanner extends ClassPathBeanDefinitionScanner {
    public QuerydslMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        addIncludeFilter(new AssignableTypeFilter(RelationalPath.class));
    }

    @Override
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, @NonNull BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = definitionHolder.getBeanDefinition();
        String beanClassName = beanDefinition.getBeanClassName();
        if (beanClassName != null) {
            try {
                Class<?> mapper = Class.forName(beanClassName);
                Class<?> entity = ResolvableType.forType(mapper.getGenericSuperclass()).getGeneric(0).resolve();
                if (entity != null) {
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(mapper);
                    builder.addConstructorArgValue(NamingUtil.pascal2camel(entity.getSimpleName()));
                    registry.registerBeanDefinition(definitionHolder.getBeanName(), builder.getRawBeanDefinition());
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public <T extends Enum<T>> EnumByNameType<T> enumType(Class<T> t) {
        return new EnumByNameType<>(t);
    }
}