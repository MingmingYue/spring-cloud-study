package com.ming.tcc.transaction.serializer;

/**
 * Create on 2019-06-08.
 */
public interface ObjectSerializer<T> {

    byte[] serialize(T t);

    T deserialize(byte[] bytes);
}
