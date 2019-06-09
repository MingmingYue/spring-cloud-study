package com.ming.tcc.transaction.order.domain.repository;

import com.ming.tcc.transaction.order.domain.entity.Shop;
import com.ming.tcc.transaction.order.infrastructure.dao.ShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Create on 2019-06-09.
 */
@Repository
public class ShopRepository {

    @Autowired
    ShopDao shopDao;

    public Shop findById(long id) {

        return shopDao.findById(id);
    }
}
