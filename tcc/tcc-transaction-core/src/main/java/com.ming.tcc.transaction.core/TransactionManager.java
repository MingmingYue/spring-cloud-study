package com.ming.tcc.transaction.core;

import com.ming.tcc.transaction.core.common.TransactionType;
import com.ming.tcc.transaction.core.exception.CancellingException;
import com.ming.tcc.transaction.core.exception.ConfirmingException;
import com.ming.tcc.transaction.core.exception.NoExistedTransactionException;
import com.ming.tcc.transaction.core.support.TransactionConfigurator;
import com.ming.tcc.transation.api.TransactionContext;
import com.ming.tcc.transation.api.TransactionStatus;
import com.ming.tcc.transation.api.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事物管理器
 * Create on 2019-06-08.
 */
public class TransactionManager {

    static final Logger log = LoggerFactory.getLogger(TransactionManager.class.getSimpleName());

    /**
     * 事务配置器
     */
    private TransactionConfigurator transactionConfigurator;

    public TransactionManager(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }

    /**
     * 定义当前线程的事务局部变量.
     */
    private ThreadLocal<Transaction> threadLocalTransaction = new ThreadLocal<Transaction>();

    /**
     * 事务开始（创建事务日志记录，并将该事务日志记录存入当前线程的事务局部变量中）
     */
    public void begin() {
        log.debug("==>begin()");
        // 事务类型为ROOT:1
        Transaction transaction = new Transaction(TransactionType.ROOT);
        log.debug("==>TransactionType:" + transaction.getTransactionType().toString() + ", Transaction Status:" + transaction.getStatus().toString());
        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        // 创建事务记录,写入事务日志库
        transactionRepository.create(transaction);
        // 将该事务日志记录存入当前线程的事务局部变量中
        threadLocalTransaction.set(transaction);
    }

    /**
     * 基于全局事务ID扩展创建新的分支事务，并存于当前线程的事务局部变量中.
     */
    public void propagationNewBegin(TransactionContext transactionContext) {
        Transaction transaction = new Transaction(transactionContext);
        log.debug("==>propagationNewBegin TransactionXid：" + UuidUtils.byteArrayToUUID(transaction.getXid().getGlobalTransactionId()).toString()
                + "|" + UuidUtils.byteArrayToUUID(transaction.getXid().getBranchQualifier()).toString());

        transactionConfigurator.getTransactionRepository().create(transaction);

        threadLocalTransaction.set(transaction);
    }

    /**
     * 找出存在的事务并处理.
     */
    public void propagationExistBegin(TransactionContext transactionContext) throws NoExistedTransactionException {
        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        Transaction transaction = transactionRepository.findByXid(transactionContext.getXid());

        if (transaction != null) {

            log.debug("==>propagationExistBegin TransactionXid：" + UuidUtils.byteArrayToUUID(transaction.getXid().getGlobalTransactionId()).toString()
                    + "|" + UuidUtils.byteArrayToUUID(transaction.getXid().getBranchQualifier()).toString());

            transaction.changeStatus(TransactionStatus.valueOf(transactionContext.getStatus()));
            threadLocalTransaction.set(transaction);
        } else {
            throw new NoExistedTransactionException();
        }
    }

    /**
     * 提交.
     */
    public void commit() {
        log.debug("==>TransactionManager commit()");
        Transaction transaction = getCurrentTransaction();

        transaction.changeStatus(TransactionStatus.CONFIRMING);
        log.debug("==>update transaction status to CONFIRMING");
        transactionConfigurator.getTransactionRepository().update(transaction);

        try {
            log.info("==>transaction begin commit()");
            transaction.commit();
            transactionConfigurator.getTransactionRepository().delete(transaction);
        } catch (Throwable commitException) {
            log.error("compensable transaction confirm failed.", commitException);
            throw new ConfirmingException(commitException);
        }
    }

    /**
     * 获取当前事务.
     */
    public Transaction getCurrentTransaction() {
        return threadLocalTransaction.get();
    }

    /**
     * 回滚事务.
     */
    public void rollback() {
        Transaction transaction = getCurrentTransaction();
        transaction.changeStatus(TransactionStatus.CANCELLING);

        transactionConfigurator.getTransactionRepository().update(transaction);

        try {
            log.info("==>transaction begin rollback()");
            transaction.rollback();
            transactionConfigurator.getTransactionRepository().delete(transaction);
        } catch (Throwable rollbackException) {
            log.error("compensable transaction rollback failed.", rollbackException);
            throw new CancellingException(rollbackException);
        }
    }
}
