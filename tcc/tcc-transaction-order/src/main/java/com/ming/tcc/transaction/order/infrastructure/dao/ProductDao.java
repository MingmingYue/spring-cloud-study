package com.ming.tcc.transaction.order.infrastructure.dao;

import com.ming.tcc.transaction.order.domain.entity.Product;

import java.util.List;

/**
 * Create on 2019-06-09.
 */
public interface ProductDao {

    Product findById(long productId);

    List<Product> findByShopId(long shopId);
}