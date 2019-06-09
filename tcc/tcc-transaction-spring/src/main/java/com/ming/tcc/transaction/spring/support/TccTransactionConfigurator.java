package com.ming.tcc.transaction.spring.support;

import com.ming.tcc.transaction.TransactionManager;
import com.ming.tcc.transaction.TransactionRepository;
import com.ming.tcc.transaction.recover.RecoverConfig;
import com.ming.tcc.transaction.support.TransactionConfigurator;
import com.ming.tcc.transaction.spring.recover.DefaultRecoverConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Tcc 事务配置器
 * Create on 2019-06-09.
 */
public class TccTransactionConfigurator implements TransactionConfigurator {

    /**
     * 事务库
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 事务恢复配置
     */
    @Autowired(required = false)
    private RecoverConfig recoverConfig = DefaultRecoverConfig.INSTANCE;

    /**
     * 根据事务配置器创建事务管理器.
     */
    private TransactionManager transactionManager = new TransactionManager(this);

    /**
     * 获取事务管理器.
     */
    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * 获取事务库.
     */
    @Override
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    /**
     * 获取事务恢复配置.
     */
    @Override
    public RecoverConfig getRecoverConfig() {
        return recoverConfig;
    }
}
