package com.ming.tcc.transaction.utils;

import com.ming.tcc.transaction.common.MethodType;
import com.ming.tcc.transation.api.TransactionContext;

/**
 * 可补偿方法工具类
 * Create on 2019-06-08.
 */
public class CompensableMethodUtil {

    /**
     * 计算方法类型.
     */
    public static MethodType calculateMethodType(TransactionContext transactionContext, boolean isCompensable) {

        if (transactionContext == null && isCompensable) {
            // 没有事务上下文信息，并且方法有事务注解的，为可补偿事务根方法（也就是事务发起者）
            //isRootTransactionMethod
            return MethodType.ROOT;
        } else if (transactionContext == null && !isCompensable) {
            // 没有事务上下文信息，并且方法没有事务注解的，为可补偿事务服务消费者（参考者）方法（一般为被调用的服务接口）
            //isSoaConsumer
            return MethodType.CONSUMER;
        } else if (transactionContext != null && isCompensable) {
            // 有事务上下文信息，并且方法有事务注解的，为可补偿事务服务提供者方法（一般为被调用的服务接口的实现方法）
            //isSoaProvider
            return MethodType.PROVIDER;
        } else {
            return MethodType.NORMAL;
        }
    }

    /**
     * 获取事务上下文参数的位置.
     */
    public static int getTransactionContextParamPosition(Class<?>[] parameterTypes) {
        int i;
        for (i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].equals(TransactionContext.class)) {
                break;
            }
        }
        return i;
    }

    /**
     * 从参数获取事务上下文.
     */
    public static TransactionContext getTransactionContextFromArgs(Object[] args) {
        TransactionContext transactionContext = null;

        for (Object arg : args) {
            if (arg != null && TransactionContext.class.isAssignableFrom(arg.getClass())) {
                transactionContext = (TransactionContext) arg;
            }
        }
        return transactionContext;
    }
}
