package com.ming.tcc.transaction.support;

import com.ming.tcc.transaction.TransactionManager;
import com.ming.tcc.transaction.TransactionRepository;
import com.ming.tcc.transaction.recover.RecoverConfig;

/**
 * 事物配置接口
 * Create on 2019-06-08.
 */
public interface TransactionConfigurator {

    /**
     * 获取事务管理器.
     */
    TransactionManager getTransactionManager();

    /**
     * 获取事务库.
     */
    TransactionRepository getTransactionRepository();

    /**
     * 获取事务恢复配置.
     */
    RecoverConfig getRecoverConfig();
}
