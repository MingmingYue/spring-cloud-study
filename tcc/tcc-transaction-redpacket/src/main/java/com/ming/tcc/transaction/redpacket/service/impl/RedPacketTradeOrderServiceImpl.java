/*
 * ========================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ========================================================================
 */
package com.ming.tcc.transaction.redpacket.service.impl;

import com.ming.tcc.transaction.Compensable;
import com.ming.tcc.transaction.redpacket.domain.entity.RedPacketAccount;
import com.ming.tcc.transaction.redpacket.domain.entity.TradeOrder;
import com.ming.tcc.transaction.redpacket.domain.repository.RedPacketAccountRepository;
import com.ming.tcc.transaction.redpacket.domain.repository.TradeOrderRepository;
import com.ming.tcc.transaction.redpacket.dto.RedPacketTradeOrderDto;
import com.ming.tcc.transaction.redpacket.service.RedPacketTradeOrderService;
import com.ming.tcc.transation.api.TransactionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create on 2019-06-09.
 */
@Service("redPacketTradeOrderService")
public class RedPacketTradeOrderServiceImpl implements RedPacketTradeOrderService {
	
	private static final Log LOG = LogFactory.getLog(RedPacketTradeOrderServiceImpl.class);

    @Autowired
    RedPacketAccountRepository redPacketAccountRepository;

    @Autowired
    TradeOrderRepository tradeOrderRepository;

    @Override
    @Compensable(confirmMethod = "confirmRecord",cancelMethod = "cancelRecord")
    @Transactional
    public String record(TransactionContext transactionContext, RedPacketTradeOrderDto tradeOrderDto) {
    	LOG.debug("==>red packet try record called");

        TradeOrder tradeOrder = new TradeOrder(
                tradeOrderDto.getSelfUserId(),
                tradeOrderDto.getOppositeUserId(),
                tradeOrderDto.getMerchantOrderNo(),
                tradeOrderDto.getAmount()
        );

        tradeOrderRepository.insert(tradeOrder);

        RedPacketAccount transferFromAccount = redPacketAccountRepository.findByUserId(tradeOrderDto.getSelfUserId());

        transferFromAccount.transferFrom(tradeOrderDto.getAmount());

        redPacketAccountRepository.save(transferFromAccount);

        return "success";
    }

    @Transactional
    public void confirmRecord(TransactionContext transactionContext, RedPacketTradeOrderDto tradeOrderDto) {
    	LOG.debug("==>red packet confirm record called");

        TradeOrder tradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        tradeOrder.confirm();
        tradeOrderRepository.update(tradeOrder);

        RedPacketAccount transferToAccount = redPacketAccountRepository.findByUserId(tradeOrderDto.getOppositeUserId());

        transferToAccount.transferTo(tradeOrderDto.getAmount());

        redPacketAccountRepository.save(transferToAccount);
    }

    @Transactional
    public void cancelRecord(TransactionContext transactionContext, RedPacketTradeOrderDto tradeOrderDto) {
    	LOG.debug("==>red packet cancel record called");

        TradeOrder tradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        if(null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.cancel();
            tradeOrderRepository.update(tradeOrder);

            RedPacketAccount capitalAccount = redPacketAccountRepository.findByUserId(tradeOrderDto.getSelfUserId());

            capitalAccount.cancelTransfer(tradeOrderDto.getAmount());

            redPacketAccountRepository.save(capitalAccount);
        }
    }
}
