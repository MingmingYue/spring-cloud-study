package com.ming.tcc.transaction.order.domain.repository;

import com.ming.tcc.transaction.order.domain.entity.Order;
import com.ming.tcc.transaction.order.domain.entity.OrderLine;
import com.ming.tcc.transaction.order.infrastructure.dao.OrderDao;
import com.ming.tcc.transaction.order.infrastructure.dao.OrderLineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Create on 2019-06-09.
 */
@Repository
public class OrderRepository {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderLineDao orderLineDao;

    /**
     * 创建订单记录.
     */
    public void createOrder(Order order) {
        orderDao.insert(order);
        for (OrderLine orderLine : order.getOrderLines()) {
            orderLineDao.insert(orderLine);
        }
    }

    /**
     * 更新订单记录.
     */
    public void updateOrder(Order order) {
        orderDao.update(order);
    }

    public Order findByMerchantOrderNo(String merchantOrderNo) {
        return orderDao.findByMerchantOrderNo(merchantOrderNo);
    }
}
