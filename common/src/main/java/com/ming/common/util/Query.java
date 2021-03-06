package com.ming.common.util;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 2019-06-05
 */
public class Query extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 1L;
    //当前页码
    @Getter
    @Setter
    private int page = 1;
    //每页条数
    @Getter
    @Setter
    private int limit = 10;

    public Query(Map<String, Object> params) {
        this.putAll(params);
        //分页参数
        if (params.get("page") != null) {
            this.page = Integer.parseInt(params.get("page").toString());
        }
        if (params.get("limit") != null) {
            this.limit = Integer.parseInt(params.get("limit").toString());
        }
        this.remove("page");
        this.remove("limit");
    }
}
