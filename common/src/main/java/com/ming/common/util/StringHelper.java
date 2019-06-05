package com.ming.common.util;

/**
 * Created by 2019-06-05
 */
public class StringHelper {

    public static String getObjectValue(Object o) {
        return o == null ? "" : o.toString();
    }
}
