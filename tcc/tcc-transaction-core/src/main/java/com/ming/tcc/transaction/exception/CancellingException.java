package com.ming.tcc.transaction.exception;

/**
 * 取消异常
 * Create on 2019-06-08.
 */
public class CancellingException extends RuntimeException {

    public CancellingException(Throwable cause) {
        super(cause);
    }
}
