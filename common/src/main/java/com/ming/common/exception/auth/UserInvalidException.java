package com.ming.common.exception.auth;

import com.ming.common.constant.CommonConstants;
import com.ming.common.exception.BaseException;

/**
 * Created by 2019-06-05
 */
public class UserInvalidException extends BaseException {

    public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_PASS_INVALID_CODE);
    }
}
