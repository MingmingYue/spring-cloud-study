package com.ming.tcc.transaction.order.api;

import com.ming.tcc.transaction.order.domain.entity.Product;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create on 2019-06-09.
 */
public interface PlaceOrderService {

    /**
     * @param payerUserId        付款者ID.
     * @param shopId             店铺ID.
     * @param productQuantities  产品数量
     * @param redPacketPayAmount 红包支付金额。
     */
    public String placeOrder(long payerUserId, long shopId, List<Pair<Long, Integer>> productQuantities, BigDecimal redPacketPayAmount);


    Product findProductById(long productId);

    List<Product> findProductByShopId(long shopId);

    BigDecimal getRedPacketAccountByUserId(long userId);

    BigDecimal getCapitalAccountByUserId(long userId);

    String getOrderStatusByMerchantOrderNo(String orderNo);
}
