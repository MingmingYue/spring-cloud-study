package com.ming.tcc.transaction.order.domain.repository;

import com.ming.tcc.transaction.order.domain.entity.Product;
import com.ming.tcc.transaction.order.infrastructure.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create on 2019-06-09.
 */
@Repository
public class ProductRepository {

    @Autowired
    ProductDao productDao;

    public Product findById(long productId) {
        return productDao.findById(productId);
    }

    public List<Product> findByShopId(long shopId) {
        return productDao.findByShopId(shopId);
    }
}