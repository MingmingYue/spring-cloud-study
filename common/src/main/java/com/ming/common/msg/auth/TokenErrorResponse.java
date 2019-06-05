package com.ming.common.msg.auth;

import com.ming.common.constant.RestCodeConstants;
import com.ming.common.msg.BaseResponse;

/**
 * Created by 2019-06-05
 */
public class TokenErrorResponse extends BaseResponse {

    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
