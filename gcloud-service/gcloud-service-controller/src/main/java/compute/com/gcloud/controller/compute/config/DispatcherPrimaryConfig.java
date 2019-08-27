package com.gcloud.controller.compute.config;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dispatcher.DispatcherSelector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class DispatcherPrimaryConfig implements ApplicationContextAware {

    @Value("${gcloud.controller.compute.dispatcherStrategy:}")
    private String dispatcherStrategy;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        DispatcherSelector selector = StringUtils.isBlank(dispatcherStrategy) ? null : DispatcherSelector.value(dispatcherStrategy);
        if(selector == null){
            selector = DispatcherSelector.SIMPLE;
        }

        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        BeanDefinition beanDefinition = registry.getBeanDefinition(StringUtils.toLowerCaseFirstOne(selector.getImpl().getSimpleName()));
        beanDefinition.setPrimary(true);

    }

}