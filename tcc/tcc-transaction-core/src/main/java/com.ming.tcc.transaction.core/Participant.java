package com.ming.tcc.transaction.core;

import com.ming.tcc.transation.api.TransactionXid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.Serializable;

/**
 * 事物参与者
 * Create on 2019-06-08.
 */
public class Participant implements Serializable {

    static final Logger log = LoggerFactory.getLogger(Participant.class.getSimpleName());

    private static final long serialVersionUID = 4127729421281425247L;

    private TransactionXid xid;

    private Terminator terminator;

    public Participant() {

    }

    public Participant(TransactionXid xid, Terminator terminator) {
        this.xid = xid;
        this.terminator = terminator;
    }

    /**
     * 回滚参与者事务（在Transaction中被调用）
     */
    public void rollback() {
        log.debug("==>Participant.rollback()");
        terminator.rollback();
    }

    /**
     * 提交参与者事务（在Transaction中被调用）.
     */
    public void commit() {
        log.debug("==>Participant.rollback()");
        terminator.commit();
    }

}