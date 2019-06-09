package com.ming.tcc.transaction;

import com.ming.tcc.transation.api.TransactionXid;

import java.util.Date;
import java.util.List;

/**
 * 事物库接口
 * Create on 2019-06-08.
 */
public interface TransactionRepository {

    /**
     * 创建事务日志记录.
     */
    int create(Transaction transaction);

    /**
     * 更新事务日志记录.
     */
    int update(Transaction transaction);

    /**
     * 删除事务日志记录.
     */
    int delete(Transaction transaction);

    /**
     * 根据xid查找事务日志记录.
     */
    Transaction findByXid(TransactionXid xid);

    /**
     * 找出所有未处理事务日志（从某一时间点开始）.
     */
    List<Transaction> findAllUnmodifiedSince(Date date);
}
