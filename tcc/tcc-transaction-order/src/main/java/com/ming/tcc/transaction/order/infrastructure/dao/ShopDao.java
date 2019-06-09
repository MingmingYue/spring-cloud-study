package com.ming.tcc.transaction.order.infrastructure.dao;


import com.ming.tcc.transaction.order.domain.entity.Shop;

/**
 * Create on 2019-06-09.
 */
public interface ShopDao {
    Shop findById(long id);
}
