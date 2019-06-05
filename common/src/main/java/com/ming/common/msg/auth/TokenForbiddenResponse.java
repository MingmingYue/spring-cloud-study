package com.ming.common.msg.auth;

import com.ming.common.constant.RestCodeConstants;
import com.ming.common.msg.BaseResponse;

/**
 * Created by 2019-06-05
 */
public class TokenForbiddenResponse extends BaseResponse {

    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
