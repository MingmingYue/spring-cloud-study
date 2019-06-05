package com.ming.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 2019-06-05
 */
public class BaseException extends RuntimeException {

    @Setter
    @Getter
    private int status = 200;

    public BaseException() {
    }

    public BaseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
