package com.ming.tcc.transaction.utils;

import java.util.Collection;

/**
 * Create on 2019-06-08.
 */
public class CollectionUtil {

    private CollectionUtil() {
    }

    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }
}
