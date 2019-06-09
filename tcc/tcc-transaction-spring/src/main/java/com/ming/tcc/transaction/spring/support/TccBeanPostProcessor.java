package com.ming.tcc.transaction.spring.support;

import com.ming.tcc.transaction.support.BeanFactory;
import com.ming.tcc.transaction.support.BeanFactoryAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Tcc bean 后置处理程序
 * Create on 2019-06-09.
 */
@Component
public class TccBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * Spring启动时加载.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        if (applicationContext.getParent() == null) {
            BeanFactoryAdapter.setBeanFactory(applicationContext.getBean(BeanFactory.class));
        }
    }
}
