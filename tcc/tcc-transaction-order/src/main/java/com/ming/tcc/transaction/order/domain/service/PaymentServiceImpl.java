package com.ming.tcc.transaction.order.domain.service;

import com.ming.tcc.transaction.Compensable;
import com.ming.tcc.transaction.capital.dto.CapitalTradeOrderDto;
import com.ming.tcc.transaction.capital.service.CapitalTradeOrderService;
import com.ming.tcc.transaction.order.domain.entity.Order;
import com.ming.tcc.transaction.order.domain.repository.OrderRepository;
import com.ming.tcc.transaction.redpacket.dto.RedPacketTradeOrderDto;
import com.ming.tcc.transaction.redpacket.service.RedPacketTradeOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Create on 2019-06-09.
 */
@Service
public class PaymentServiceImpl {

    private static final Log log = LogFactory.getLog(PaymentServiceImpl.class);

    @Autowired
    CapitalTradeOrderService capitalTradeOrderService;

    @Autowired
    RedPacketTradeOrderService redPacketTradeOrderService;

    @Autowired
    OrderRepository orderRepository;

    /*
     * 如果事务日志没有成功提交，那么整个TCC事务将会需要恢复，
     * 如果是在CONFIRMING阶段出异常，则恢复Job将继续启动事务的Confirm操作过程，
     * 如果是在TRYING阶段出异常，则恢复Job将启动事务的Cancel操作过程。
     */

    /**
     * 付款.
     *
     * @param order              订单信息.
     * @param redPacketPayAmount 红包支付金额
     * @param capitalPayAmount   资金帐户支付金额.
     */
    @Compensable(confirmMethod = "confirmMakePayment", cancelMethod = "cancelMakePayment")
    @Transactional(rollbackFor = {Exception.class})
    public void makePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {
        log.debug("==>order try make payment called");
        log.debug("==>redPacketPayAmount：" + redPacketPayAmount.doubleValue());
        log.debug("==>capitalPayAmount：" + capitalPayAmount.doubleValue());

        order.pay(redPacketPayAmount, capitalPayAmount);
        orderRepository.updateOrder(order);

        log.debug("==try capitalTradeOrderService.record(null, buildCapitalTradeOrderDto(order) begin");
        // 资金帐户交易订单记录（因为此方法中有TransactionContext参数，因此也会被TccTransactionContextAspect拦截处理）
        String result = capitalTradeOrderService.record(null, buildCapitalTradeOrderDto(order));
        log.debug("==try capitalTradeOrderService.record(null, buildCapitalTradeOrderDto(order) end, result:" + result);

        log.debug("==>try redPacketTradeOrderService.record(null, buildRedPacketTradeOrderDto(order)) begin");
        // 红包帐户交易订单记录
        String result2 = redPacketTradeOrderService.record(null, buildRedPacketTradeOrderDto(order));
        log.debug("==>try redPacketTradeOrderService.record(null, buildRedPacketTradeOrderDto(order)) end, result:" + result2);

        log.debug("==>order try end");

    }

    /**
     * 确认付款.
     */
    public void confirmMakePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {

        log.debug("==>order confirm make payment called, set status : CONFIRMED");
        order.confirm(); // 设置订单状态为CONFIRMED

        orderRepository.updateOrder(order);
    }

    /**
     * 取消付款.
     */
    public void cancelMakePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {

        log.debug("==>order cancel make payment called, set status : PAY_FAILED");

        order.cancelPayment();

        orderRepository.updateOrder(order);
    }


    /**
     * 构建资金帐户支付订单Dto
     */
    private CapitalTradeOrderDto buildCapitalTradeOrderDto(Order order) {
        log.debug("==>buildCapitalTradeOrderDto(Order order)");
        CapitalTradeOrderDto tradeOrderDto = new CapitalTradeOrderDto();
        tradeOrderDto.setAmount(order.getCapitalPayAmount());
        tradeOrderDto.setMerchantOrderNo(order.getMerchantOrderNo());
        tradeOrderDto.setSelfUserId(order.getPayerUserId());
        tradeOrderDto.setOppositeUserId(order.getPayeeUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getMerchantOrderNo()));
        return tradeOrderDto;
    }

    /**
     * 构建红包帐户支付订单Dto
     */
    private RedPacketTradeOrderDto buildRedPacketTradeOrderDto(Order order) {
        log.debug("==>buildRedPacketTradeOrderDto(Order order)");
        RedPacketTradeOrderDto tradeOrderDto = new RedPacketTradeOrderDto();
        tradeOrderDto.setAmount(order.getRedPacketPayAmount());
        tradeOrderDto.setMerchantOrderNo(order.getMerchantOrderNo());
        tradeOrderDto.setSelfUserId(order.getPayerUserId());
        tradeOrderDto.setOppositeUserId(order.getPayeeUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getMerchantOrderNo()));
        return tradeOrderDto;
    }
}
