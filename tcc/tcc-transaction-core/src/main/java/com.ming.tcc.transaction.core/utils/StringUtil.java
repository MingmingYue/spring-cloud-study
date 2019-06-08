package com.ming.tcc.transaction.core.utils;

/**
 * Create on 2019-06-08.
 */
public class StringUtil {

    public static boolean isNotEmpty(String value) {
        if (value == null) {
            return false;
        }
        return !"".equals(value);
    }
}
