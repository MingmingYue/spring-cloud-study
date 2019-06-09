package com.ming.tcc.transaction.capital.domain.repository;

import com.ming.tcc.transaction.capital.domain.entity.TradeOrder;
import com.ming.tcc.transaction.capital.infrastruture.dao.TradeOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Create on 2019-06-09.
 */
@Repository
public class TradeOrderRepository {

    @Autowired
    TradeOrderDao tradeOrderDao;

    public void insert(TradeOrder tradeOrder) {
        tradeOrderDao.insert(tradeOrder);
    }

    public void update(TradeOrder tradeOrder) {
        tradeOrderDao.update(tradeOrder);
    }

    public TradeOrder findByMerchantOrderNo(String merchantOrderNo) {
        return tradeOrderDao.findByMerchantOrderNo(merchantOrderNo);
    }
}