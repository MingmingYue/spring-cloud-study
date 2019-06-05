package com.ming.common.exception.auth;

import com.ming.common.constant.CommonConstants;
import com.ming.common.exception.BaseException;

/**
 * Created by 2019-06-05
 */
public class ClientForbiddenException extends BaseException {

    public ClientForbiddenException(String message) {
        super(message, CommonConstants.EX_CLIENT_FORBIDDEN_CODE);
    }
}
