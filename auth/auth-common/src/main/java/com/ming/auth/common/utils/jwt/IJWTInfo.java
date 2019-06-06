package com.ming.auth.common.utils.jwt;

/**
 * Created by 2019-06-06
 */
public interface IJWTInfo {
    /**
     * 获取用户名
     */
    String getUniqueName();

    /**
     * 获取用户ID
     */
    String getId();

    /**
     * 获取名称
     */
    String getName();
}
