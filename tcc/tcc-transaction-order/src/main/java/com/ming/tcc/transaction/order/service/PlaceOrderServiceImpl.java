package com.ming.tcc.transaction.order.service;

import com.ming.tcc.transaction.exception.CancellingException;
import com.ming.tcc.transaction.exception.ConfirmingException;
import com.ming.tcc.transaction.order.api.PlaceOrderService;
import com.ming.tcc.transaction.order.domain.entity.Order;
import com.ming.tcc.transaction.order.domain.entity.Product;
import com.ming.tcc.transaction.order.domain.entity.Shop;
import com.ming.tcc.transaction.order.domain.repository.ProductRepository;
import com.ming.tcc.transaction.order.domain.repository.ShopRepository;
import com.ming.tcc.transaction.order.domain.service.AccountServiceImpl;
import com.ming.tcc.transaction.order.domain.service.OrderServiceImpl;
import com.ming.tcc.transaction.order.domain.service.PaymentServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create on 2019-06-09.
 */
@Service("placeOrderService")
public class PlaceOrderServiceImpl implements PlaceOrderService {

    private static final Log LOG = LogFactory.getLog(PlaceOrderServiceImpl.class);

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AccountServiceImpl accountService;


    /**
     * 下订单。
     *
     * @param payerUserId        付款者ID.
     * @param shopId             店铺ID.
     * @param productQuantities  产品数量
     * @param redPacketPayAmount 红包支付金额。
     */
    @Override
    public String placeOrder(long payerUserId, long shopId, List<Pair<Long, Integer>> productQuantities, BigDecimal redPacketPayAmount) {

        // 查找店铺信息，主要目的用于获取收款者ID
        Shop shop = shopRepository.findById(shopId);
        // 创建订单
        Order order = orderService.createOrder(payerUserId, shop.getOwnerUserId(), productQuantities);
        LOG.debug("==>placeOrder, shopId:" + shopId + ", payerUserId:" + payerUserId + ", payeeUserId:" + shop.getOwnerUserId());

        Boolean result = false;

        try {
            // 付款
            paymentService.makePayment(order, redPacketPayAmount, order.getTotalAmount().subtract(redPacketPayAmount));

        } catch (ConfirmingException confirmingException) {
            //exception throws with the tcc transaction status is CONFIRMING,
            //when tcc transaction is confirming status,
            // the tcc transaction recovery will try to confirm the whole transaction to ensure eventually consistent.

            result = true;
        } catch (CancellingException cancellingException) {
            //exception throws with the tcc transaction status is CANCELLING,
            //when tcc transaction is under CANCELLING status,
            // the tcc transaction recovery will try to cancel the whole transaction to ensure eventually consistent.
        } catch (Throwable e) {
            //other exceptions throws at TRYING stage.
            //you can retry or cancel the operation.
        }

        return order.getMerchantOrderNo();

    }

    @Override
    public Product findProductById(long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> findProductByShopId(long shopId) {
        return productRepository.findByShopId(shopId);
    }

    @Override
    public BigDecimal getRedPacketAccountByUserId(long userId) {
        return accountService.getRedPacketAccountByUserId(userId);
    }

    @Override
    public BigDecimal getCapitalAccountByUserId(long userId) {
        return accountService.getCapitalAccountByUserId(userId);
    }

    @Override
    public String getOrderStatusByMerchantOrderNo(String orderNo) {
        return orderService.getOrderStatusByMerchantOrderNo(orderNo);
    }
}
