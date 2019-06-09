package com.ming.tcc.transaction.redpacket.infrastructure.dao;

import com.ming.tcc.transaction.redpacket.domain.entity.TradeOrder;

/**
 * Create on 2019-06-09.
 */
public interface TradeOrderDao {

    void insert(TradeOrder tradeOrder);

    void update(TradeOrder tradeOrder);

    TradeOrder findByMerchantOrderNo(String merchantOrderNo);
}