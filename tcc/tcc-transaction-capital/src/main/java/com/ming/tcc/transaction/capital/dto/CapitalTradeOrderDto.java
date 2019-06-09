package com.ming.tcc.transaction.capital.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Create on 2019-06-09.
 */
public class CapitalTradeOrderDto implements Serializable {

    private static final long serialVersionUID = 6627401903410124642L;
    @Getter
    @Setter
    private long selfUserId;

    /**
     * 对方账号UserId
     */
    @Getter
    @Setter
    private long oppositeUserId;
    @Getter
    @Setter
    private String orderTitle;
    @Getter
    @Setter
    private String merchantOrderNo;
    @Getter
    @Setter
    private BigDecimal amount;
}
