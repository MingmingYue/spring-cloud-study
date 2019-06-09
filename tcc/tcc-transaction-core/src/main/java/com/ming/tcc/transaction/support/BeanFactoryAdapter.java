package com.ming.tcc.transaction.support;

/**
 * Create on 2019-06-08.
 */
public class BeanFactoryAdapter {

    private static BeanFactory beanFactory;

    public static Object getBean(Class<?> aClass) {
        return beanFactory.getBean(aClass);
    }

    public static void setBeanFactory(BeanFactory beanFactory) {
        BeanFactoryAdapter.beanFactory = beanFactory;
    }
}
