package com.ming.tcc.transaction.spring.support;

import com.ming.tcc.transaction.support.BeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 应用上下文
 * Create on 2019-06-09.
 */
@Component
public class TccApplicationContext implements BeanFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getBean(Class<?> aClass) {
        return this.applicationContext.getBean(aClass);
    }
}
