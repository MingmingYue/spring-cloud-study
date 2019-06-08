package com.ming.tcc.transaction.core.interceptor;

import com.ming.tcc.transaction.core.common.MethodType;
import com.ming.tcc.transaction.core.exception.NoExistedTransactionException;
import com.ming.tcc.transaction.core.exception.OptimisticLockException;
import com.ming.tcc.transaction.core.support.TransactionConfigurator;
import com.ming.tcc.transaction.core.utils.CompensableMethodUtil;
import com.ming.tcc.transaction.core.utils.ReflectionUtil;
import com.ming.tcc.transation.api.TransactionContext;
import com.ming.tcc.transation.api.TransactionStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 可补偿事物拦截器
 * Create on 2019-06-08.
 */
public class CompensableTransactionInterceptor {

    static final Logger logger = LoggerFactory.getLogger(CompensableTransactionInterceptor.class.getSimpleName());

    /**
     * 事务配置器
     */
    private TransactionConfigurator transactionConfigurator;

    /**
     * 设置事务配置器.
     */
    public void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }

    /**
     * 拦截补偿方法.
     */
    public Object interceptCompensableMethod(ProceedingJoinPoint pjp) throws Throwable {
        // 从拦截方法的参数中获取事务上下文
        TransactionContext transactionContext = CompensableMethodUtil.getTransactionContextFromArgs(pjp.getArgs());
        // 计算可补偿事务方法类型
        MethodType methodType = CompensableMethodUtil.calculateMethodType(transactionContext, true);
        logger.debug("==>interceptCompensableMethod methodType:" + methodType.toString());
        switch (methodType) {
            case ROOT:
                // 主事务方法的处理
                return rootMethodProceed(pjp);
            case PROVIDER:
                // 服务提供者事务方法处理
                return providerMethodProceed(pjp, transactionContext);
            default:
                // 其他的方法都是直接执行
                return pjp.proceed();
        }
    }

    /**
     * 主事务方法的处理.
     */
    private Object rootMethodProceed(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("==>rootMethodProceed");
        // 事务开始（创建事务日志记录，并在当前线程缓存该事务日志记录）
        transactionConfigurator.getTransactionManager().begin();
        // 返回值
        Object returnValue = null;
        try {

            logger.debug("==>rootMethodProceed try begin");
            // Try (开始执行被拦截的方法)
            returnValue = pjp.proceed();
            logger.debug("==>rootMethodProceed try end");
        } catch (OptimisticLockException e) {
            logger.warn("==>compensable transaction trying exception.", e);
            //do not rollback, waiting for recovery job
            throw e;
        } catch (Throwable tryingException) {
            logger.warn("compensable transaction trying failed.", tryingException);
            transactionConfigurator.getTransactionManager().rollback();
            throw tryingException;
        }
        logger.info("===>rootMethodProceed begin commit()");
        // Try检验正常后提交(事务管理器在控制提交)
        transactionConfigurator.getTransactionManager().commit();
        return returnValue;
    }

    /**
     * 服务提供者事务方法处理.
     */
    private Object providerMethodProceed(ProceedingJoinPoint pjp, TransactionContext transactionContext) throws Throwable {

        logger.debug("==>providerMethodProceed transactionStatus:" + TransactionStatus.valueOf(transactionContext.getStatus()).toString());

        switch (TransactionStatus.valueOf(transactionContext.getStatus())) {
            case TRYING:
                logger.debug("==>providerMethodProceed try begin");
                // 基于全局事务ID扩展创建新的分支事务，并存于当前线程的事务局部变量中.
                transactionConfigurator.getTransactionManager().propagationNewBegin(transactionContext);
                logger.debug("==>providerMethodProceed try end");
                return pjp.proceed();
            case CONFIRMING:
                try {
                    logger.debug("==>providerMethodProceed confirm begin");
                    // 找出存在的事务并处理.
                    transactionConfigurator.getTransactionManager().propagationExistBegin(transactionContext);
                    transactionConfigurator.getTransactionManager().commit(); // 提交
                    logger.debug("==>providerMethodProceed confirm end");
                } catch (NoExistedTransactionException excepton) {
                    //the transaction has been commit,ignore it.
                }
                break;
            case CANCELLING:
                try {
                    logger.debug("==>providerMethodProceed cancel begin");
                    transactionConfigurator.getTransactionManager().propagationExistBegin(transactionContext);
                    transactionConfigurator.getTransactionManager().rollback(); // 回滚
                    logger.debug("==>providerMethodProceed cancel end");
                } catch (NoExistedTransactionException exception) {
                    //the transaction has been rollback,ignore it.
                }
                break;
            default:
                break;
        }
        Method method = ((MethodSignature) (pjp.getSignature())).getMethod();
        return ReflectionUtil.getNullValue(method.getReturnType());
    }
}
