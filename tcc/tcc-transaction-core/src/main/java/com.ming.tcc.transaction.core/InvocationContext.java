package com.ming.tcc.transaction.core;

import lombok.Getter;

import java.io.Serializable;

/**
 * 方法调用上下文
 * Create on 2019-06-08.
 */
public class InvocationContext implements Serializable {

    private static final long serialVersionUID = -7969140711432461165L;
    @Getter
    private Class targetClass;
    @Getter
    private String methodName;
    @Getter
    private Class[] parameterTypes;
    @Getter
    private Object[] args;

    public InvocationContext() {
    }

    public InvocationContext(Class targetClass, String methodName, Class[] parameterTypes, Object... args) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.targetClass = targetClass;
        this.args = args;
    }
}
