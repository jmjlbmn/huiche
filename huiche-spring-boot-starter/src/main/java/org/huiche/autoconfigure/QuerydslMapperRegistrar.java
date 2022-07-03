package org.huiche.autoconfigure;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Maning
 */
public class QuerydslMapperRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata, @NotNull BeanDefinitionRegistry registry) {
        QuerydslMapperScanner scanner = new QuerydslMapperScanner(registry);
        scanner.scan(AutoConfigurationPackages.get(beanFactory).toArray(new String[0]));
    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}