package com.ming.common.msg;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 2019-06-05
 */
@Getter
@Setter
public class BaseResponse {

    private int status = 200;
    private String message;

    BaseResponse() {
    }

    public BaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
