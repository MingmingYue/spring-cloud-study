package com.ming.tcc.transaction.capital.infrastruture.dao;

import com.ming.tcc.transaction.capital.domain.entity.TradeOrder;

/**
 * Create on 2019-06-09.
 */
public interface TradeOrderDao {

    void insert(TradeOrder tradeOrder);

    void update(TradeOrder tradeOrder);

    TradeOrder findByMerchantOrderNo(String merchantOrderNo);
}
