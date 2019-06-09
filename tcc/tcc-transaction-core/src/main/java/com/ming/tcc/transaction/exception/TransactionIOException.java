package com.ming.tcc.transaction.exception;

/**
 * 事物IO异常
 * Create on 2019-06-08.
 */
public class TransactionIOException extends RuntimeException {

    private static final long serialVersionUID = 6508064607297986329L;

    public TransactionIOException(String message) {
        super(message);
    }

    public TransactionIOException(Throwable e) {
        super(e);
    }
}