package com.ming.tcc.transaction.utils;

import java.lang.reflect.Method;

/**
 * Create on 2019-06-08.
 */
public class ReflectionUtil {

    public static Class getDeclaringType(Class aClass, String methodName, Class<?>[] parameterTypes) {
        Method method = null;
        Class findClass = aClass;

        do {
            Class[] clazzes = findClass.getInterfaces();
            for (Class clazz : clazzes) {
                try {
                    method = clazz.getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    method = null;
                }
                if (method != null) {
                    return clazz;
                }
            }
            findClass = findClass.getSuperclass();
        } while (!findClass.equals(Object.class));
        return aClass;
    }

    public static Object getNullValue(Class type) {

        if (boolean.class.equals(type)) {
            return false;
        } else if (byte.class.equals(type)) {
            return 0;
        } else if (short.class.equals(type)) {
            return 0;
        } else if (int.class.equals(type)) {
            return 0;
        } else if (long.class.equals(type)) {
            return 0;
        } else if (float.class.equals(type)) {
            return 0;
        } else if (double.class.equals(type)) {
            return 0;
        }
        return null;
    }
}
