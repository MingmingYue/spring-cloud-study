package com.ming.common.exception.auth;

import com.ming.common.constant.CommonConstants;
import com.ming.common.exception.BaseException;

/**
 * Created by 2019-06-05
 */
public class UserTokenException extends BaseException {

    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
