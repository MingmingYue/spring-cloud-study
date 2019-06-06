package com.ming.auth.client.exception;

/**
 * Created by 2019-06-06
 */
public class JwtTokenExpiredException extends Exception {
    public JwtTokenExpiredException(String s) {
        super(s);
    }
}
