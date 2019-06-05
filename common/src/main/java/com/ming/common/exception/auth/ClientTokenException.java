package com.ming.common.exception.auth;

import com.ming.common.constant.CommonConstants;
import com.ming.common.exception.BaseException;

/**
 * Created by 2019-06-05
 */
public class ClientTokenException extends BaseException {

    public ClientTokenException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
