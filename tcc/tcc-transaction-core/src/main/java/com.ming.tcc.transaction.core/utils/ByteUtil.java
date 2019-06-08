package com.ming.tcc.transaction.core.utils;

import java.nio.ByteBuffer;

/**
 * Create on 2019-06-08.
 */
public class ByteUtil {

    public static byte[] longToBytes(long l) {
        return ByteBuffer.allocate(8).putLong(l).array();
    }

    public static long bytesToLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }
}
