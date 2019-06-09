package com.ming.tcc.transaction.order.domain.service;

import com.ming.tcc.transaction.order.domain.entity.Order;
import com.ming.tcc.transaction.order.domain.factory.OrderFactory;
import com.ming.tcc.transaction.order.domain.repository.OrderRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Create on 2019-06-09.
 */
@Service
public class OrderServiceImpl {

    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    OrderFactory orderFactory;

    /**
     * 下订单.
     * @param payerUserId 付款者ID.
     * @param payeeUserId 收款者ID.
     * @param productQuantities 商品列表（数量，单价）
     * @return
     */
    @Transactional
    public Order createOrder(long payerUserId, long payeeUserId, List<Pair<Long, Integer>> productQuantities) {
        Order order = orderFactory.buildOrder(payerUserId, payeeUserId, productQuantities);

        orderRepository.createOrder(order);

        return order;
    }
    
    public String getOrderStatusByMerchantOrderNo(String orderNo){
        return orderRepository.findByMerchantOrderNo(orderNo).getStatus();
    }
}
