package com.ming.tcc.transaction.core.recover;

/**
 * 事务恢复配置接口.
 * Create on 2019-06-08.
 */
public interface RecoverConfig {

    /**
     * 获取最大重试次数
     */
    int getMaxRetryCount();

    /**
     * 获取需要执行事务恢复的持续时间.
     */
    int getRecoverDuration();

    /**
     * 获取定时任务规则表达式.
     */
    String getCronExpression();
}
