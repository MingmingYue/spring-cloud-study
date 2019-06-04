package com.ming.common.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by 2019-06-04
 */
public class BlankUtil {

    public static boolean isBlank(final String str) {
        return (str == null) || (str.trim().length() <= 0);
    }

    public static boolean isBlank(final Character cha) {
        return (cha == null) || cha.equals(' ');
    }

    public static boolean isBlank(final Object obj) {
        return (obj == null);
    }

    public static boolean isBlank(final Object[] objs) {
        return (objs == null) || (objs.length <= 0);
    }

    public static boolean isBlank(final Collection<?> obj) {
        return (obj == null) || (obj.size() <= 0);
    }

    public static boolean isBlank(final Set<?> obj) {
        return (obj == null) || (obj.size() <= 0);
    }

    public static boolean isBlank(final Serializable obj) {
        return obj == null;
    }

    public static boolean isBlank(final Map<?, ?> obj) {
        return (obj == null) || (obj.size() <= 0);
    }

    /**
     * 判断字符串是否全为数字
     * @param str 待检查的字符串
     * @return true 不全为数字
     * 			false 全为数字
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return !pattern.matcher(str).matches();
    }
}
