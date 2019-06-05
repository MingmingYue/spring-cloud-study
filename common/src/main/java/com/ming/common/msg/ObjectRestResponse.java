package com.ming.common.msg;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 2019-06-05
 */
@Getter@Setter
public class ObjectRestResponse<T> extends BaseResponse {

    T data;
    boolean rel;

    public ObjectRestResponse rel(boolean rel) {
        this.setRel(rel);
        return this;
    }

    public ObjectRestResponse data(T data) {
        this.setData(data);
        return this;
    }
}
