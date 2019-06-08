package com.ming.tcc.transaction.core.common;

import lombok.Getter;

/**
 * 事物类别
 * Create on 2019-06-08.
 */
public enum TransactionType {

    /**
     * 主事务:1.
     */
    ROOT(1),

    /**
     * 分支事务:2.
     */
    BRANCH(2);

    @Getter
    int id;

    TransactionType(int id) {
        this.id = id;
    }

    public static TransactionType valueOf(int id) {
        switch (id) {
            case 1:
                return ROOT;
            case 2:
                return BRANCH;
            default:
                return null;
        }
    }

}