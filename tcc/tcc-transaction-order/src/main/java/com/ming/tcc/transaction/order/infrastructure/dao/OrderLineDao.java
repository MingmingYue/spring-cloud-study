package com.ming.tcc.transaction.order.infrastructure.dao;


import com.ming.tcc.transaction.order.domain.entity.OrderLine;

/**
 * Create on 2019-06-09.
 */
public interface OrderLineDao {
    void insert(OrderLine orderLine);
}
