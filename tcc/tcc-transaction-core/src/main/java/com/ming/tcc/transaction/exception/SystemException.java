package com.ming.tcc.transaction.exception;

/**
 * 系统异常
 * Create on 2019-06-08.
 */
public class SystemException extends RuntimeException {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable e) {
        super(e);
    }

    public SystemException(String message, Throwable e) {
        super(message, e);
    }
}
