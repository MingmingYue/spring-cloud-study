package com.ming.tcc.transaction.order.infrastructure.dao;


import com.ming.tcc.transaction.order.domain.entity.Order;

/**
 * Create on 2019-06-09.
 */
public interface OrderDao {

    void insert(Order order);

    void update(Order order);

    Order findByMerchantOrderNo(String merchantOrderNo);
}
