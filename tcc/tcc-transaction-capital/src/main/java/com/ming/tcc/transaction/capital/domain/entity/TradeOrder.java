package com.ming.tcc.transaction.capital.domain.entity;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * Create on 2019-06-09.
 */
public class TradeOrder {

    @Getter
    private long id;
    @Getter
    private long selfUserId;
    @Getter
    private long oppositeUserId;
    @Getter
    private String merchantOrderNo;
    @Getter
    private BigDecimal amount;
    @Getter
    private String status = "DRAFT";

    public TradeOrder() {
    }

    public TradeOrder(long selfUserId, long oppositeUserId, String merchantOrderNo, BigDecimal amount) {
        this.selfUserId = selfUserId;
        this.oppositeUserId = oppositeUserId;
        this.merchantOrderNo = merchantOrderNo;
        this.amount = amount;
    }

    public void confirm() {
        this.status = "CONFIRM";
    }

    public void cancel() {
        this.status = "CANCEL";
    }
}
