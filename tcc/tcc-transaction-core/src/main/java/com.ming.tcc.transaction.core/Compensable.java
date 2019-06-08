package com.ming.tcc.transaction.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事物补偿注解
 * Create on 2019-06-08.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Compensable {

    String confirmMethod() default "";

    String cancelMethod() default "";
}
