package com.ming.tcc.transaction.core.interceptor;

import com.ming.tcc.transaction.core.TransactionRepository;
import com.ming.tcc.transaction.core.Compensable;
import com.ming.tcc.transaction.core.InvocationContext;
import com.ming.tcc.transaction.core.Participant;
import com.ming.tcc.transaction.core.Terminator;
import com.ming.tcc.transaction.core.Transaction;
import com.ming.tcc.transaction.core.common.MethodType;
import com.ming.tcc.transaction.core.support.TransactionConfigurator;
import com.ming.tcc.transaction.core.utils.CompensableMethodUtil;
import com.ming.tcc.transaction.core.utils.ReflectionUtil;
import com.ming.tcc.transation.api.TransactionContext;
import com.ming.tcc.transation.api.TransactionStatus;
import com.ming.tcc.transation.api.TransactionXid;
import com.ming.tcc.transation.api.UuidUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 资源协调拦截器.
 * Create on 2019-06-08.
 */
public class ResourceCoordInatorInterceptor {

    static final Logger log = LoggerFactory.getLogger(ResourceCoordInatorInterceptor.class.getSimpleName());

    /**
     * 事务配置器.
     */
    private TransactionConfigurator transactionConfigurator;

    /**
     * 设置事务配置器.
     */
    public void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }

    /**
     * 拦截事务上下文方法.
     */
    public Object interceptTransactionContextMethod(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("==>interceptTransactionContextMethod(ProceedingJoinPoint pjp)");
        // 获取当前事务
        Transaction transaction = transactionConfigurator.getTransactionManager().getCurrentTransaction();

        // Trying(判断是否Try阶段的事务)
        if (transaction != null && transaction.getStatus().equals(TransactionStatus.TRYING)) {
            log.debug("==>TransactionStatus:" + transaction.getStatus().toString());
            // 从参数获取事务上下文
            TransactionContext transactionContext = CompensableMethodUtil.getTransactionContextFromArgs(pjp.getArgs());
            // 获取事务补偿注解
            Compensable compensable = getCompensable(pjp);
            // 计算方法类型
            MethodType methodType = CompensableMethodUtil.calculateMethodType(transactionContext, compensable != null ? true : false);
            log.debug("==>methodType:" + methodType.toString());

            switch (methodType) {
                case ROOT:
                    // 生成和登记根参与者
                    generateAndEnlistRootParticipant(pjp);
                    break;
                case CONSUMER:
                    // 生成并登记消费者的参与者
                    generateAndEnlistConsumerParticipant(pjp);
                    break;
                case PROVIDER:
                    // 生成并登记服务提供者的参与者
                    generateAndEnlistProviderParticipant(pjp);
                    break;
                default:
                    break;
            }
        }
        log.debug("==>pjp.proceed(pjp.getArgs())");
        return pjp.proceed(pjp.getArgs());
    }

    /**
     * 生成和登记根参与者.
     */
    private Participant generateAndEnlistRootParticipant(ProceedingJoinPoint pjp) {
        log.debug("==>generateAndEnlistRootParticipant(ProceedingJoinPoint pjp)");
        return getParticipant(pjp);
    }

    /**
     * 生成并登记消费者的参与者
     */
    private Participant generateAndEnlistConsumerParticipant(ProceedingJoinPoint pjp) {
        log.debug("==>generateAndEnlistConsumerParticipant(ProceedingJoinPoint pjp)");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        // 获取当前事务
        Transaction transaction = transactionConfigurator.getTransactionManager().getCurrentTransaction();
        // 获取事务Xid
        TransactionXid xid = new TransactionXid(transaction.getXid().getGlobalTransactionId());
        log.debug("==>TransactionXid：" + UuidUtils.byteArrayToUUID(xid.getGlobalTransactionId()).toString()
                + "|" + UuidUtils.byteArrayToUUID(xid.getBranchQualifier()).toString());

        // 获取事务上下文参数的位置
        int position = CompensableMethodUtil.getTransactionContextParamPosition(((MethodSignature) pjp.getSignature()).getParameterTypes());

        // 给服务接口的TransactionContext参数设值  构建事务上下文
        pjp.getArgs()[position] = new TransactionContext(xid, transaction.getStatus().getId());
        // 获取服务接口参数
        Object[] tryArgs = pjp.getArgs();
        // 确认提交参数
        Object[] confirmArgs = new Object[tryArgs.length];
        // 取消提交参数
        Object[] cancelArgs = new Object[tryArgs.length];
        // 数组拷贝
        System.arraycopy(tryArgs, 0, confirmArgs, 0, tryArgs.length);
        // 构建事务确认上下文
        confirmArgs[position] = new TransactionContext(xid, TransactionStatus.CONFIRMING.getId());
        // 数组拷贝
        System.arraycopy(tryArgs, 0, cancelArgs, 0, tryArgs.length);
        // 构建事务取消上下文
        cancelArgs[position] = new TransactionContext(xid, TransactionStatus.CANCELLING.getId());

        Class targetClass = ReflectionUtil.getDeclaringType(pjp.getTarget().getClass(), method.getName(), method.getParameterTypes());

        // 构建确认方法的提交上下文
        InvocationContext confirmInvocation = new InvocationContext(targetClass, method.getName(), method.getParameterTypes(), confirmArgs);
        // 构建取消方法的提交上下文
        InvocationContext cancelInvocation = new InvocationContext(targetClass, method.getName(), method.getParameterTypes(), cancelArgs);

        // 构建参与者对像
        Participant participant =
                new Participant(
                        xid,
                        new Terminator(confirmInvocation, cancelInvocation));
        // 加入到参与者
        transaction.enlistParticipant(participant);

        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        // 更新事务
        transactionRepository.update(transaction);

        return participant;
    }

    /**
     * 生成并登记服务提供者的参与者
     */
    private Participant generateAndEnlistProviderParticipant(ProceedingJoinPoint pjp) {
        log.debug("==>generateAndEnlistProviderParticipant(ProceedingJoinPoint pjp)");
        return getParticipant(pjp);
    }

    private Participant getParticipant(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Compensable compensable = getCompensable(pjp);
        // 确认方法
        String confirmMethodName = compensable.confirmMethod();
        // 取消方法
        String cancelMethodName = compensable.cancelMethod();
        // 获取当前事务
        Transaction transaction = transactionConfigurator.getTransactionManager().getCurrentTransaction();
        // 获取事务Xid
        TransactionXid xid = new TransactionXid(transaction.getXid().getGlobalTransactionId());
        log.debug("==>TransactionXid：" + UuidUtils.byteArrayToUUID(xid.getGlobalTransactionId()).toString()
                + "|" + UuidUtils.byteArrayToUUID(xid.getBranchQualifier()).toString());

        Class targetClass = ReflectionUtil.getDeclaringType(pjp.getTarget().getClass(), method.getName(), method.getParameterTypes());

        // 构建确认方法的提交上下文
        InvocationContext confirmInvocation = new InvocationContext(targetClass, confirmMethodName,
                method.getParameterTypes(), pjp.getArgs());

        // 构建取消方法的提交上下文
        InvocationContext cancelInvocation = new InvocationContext(targetClass, cancelMethodName,
                method.getParameterTypes(), pjp.getArgs());

        // 构建参与者对像   事务的ID不变，参与者的分支ID是新生成的
        Participant participant =
                new Participant(
                        xid,
                        new Terminator(confirmInvocation, cancelInvocation));
        // 加入参与者
        transaction.enlistParticipant(participant);
        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        // 更新事务
        transactionRepository.update(transaction);
        return participant;
    }

    /**
     * 根据切点，获取事务注解.
     */
    private Compensable getCompensable(ProceedingJoinPoint pjp) {
        log.debug("==>getCompensable(ProceedingJoinPoint pjp)");
        // 获取签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取方法
        Method method = signature.getMethod();
        // 获取注解
        Compensable compensable = method.getAnnotation(Compensable.class);

        if (compensable == null) {
            Method targetMethod = null;
            try {
                // 获取目标方法
                targetMethod = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
                if (targetMethod != null) {
                    compensable = targetMethod.getAnnotation(Compensable.class);
                }
            } catch (NoSuchMethodException e) {
                compensable = null;
            }
        }
        return compensable;
    }
}
