package com.ming.tcc.transaction.capital.service.impl;

import com.ming.tcc.transaction.Compensable;
import com.ming.tcc.transaction.capital.domain.entity.CapitalAccount;
import com.ming.tcc.transaction.capital.domain.entity.TradeOrder;
import com.ming.tcc.transaction.capital.domain.repository.CapitalAccountRepository;
import com.ming.tcc.transaction.capital.domain.repository.TradeOrderRepository;
import com.ming.tcc.transaction.capital.dto.CapitalTradeOrderDto;
import com.ming.tcc.transaction.capital.service.CapitalTradeOrderService;
import com.ming.tcc.transation.api.TransactionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create on 2019-06-09.
 */
@Service("capitalTradeOrderService")
public class CapitalTradeOrderServiceImpl implements CapitalTradeOrderService {

    private static final Log LOG = LogFactory.getLog(CapitalTradeOrderServiceImpl.class);

    @Autowired
    CapitalAccountRepository capitalAccountRepository;

    @Autowired
    TradeOrderRepository tradeOrderRepository;

    @Override
    @Compensable(confirmMethod = "confirmRecord", cancelMethod = "cancelRecord")
    @Transactional
    public String record(TransactionContext transactionContext, CapitalTradeOrderDto tradeOrderDto) {
        LOG.debug("==>capital try record called");

        TradeOrder tradeOrder = new TradeOrder(
                tradeOrderDto.getSelfUserId(),
                tradeOrderDto.getOppositeUserId(),
                tradeOrderDto.getMerchantOrderNo(),
                tradeOrderDto.getAmount()
        );
        tradeOrderRepository.insert(tradeOrder);

        CapitalAccount transferFromAccount = capitalAccountRepository.findByUserId(tradeOrderDto.getSelfUserId());
        // 先扣减付款方资金帐户资金
        transferFromAccount.transferFrom(tradeOrderDto.getAmount());

        capitalAccountRepository.save(transferFromAccount);
        return "success";
    }

    @Transactional
    public void confirmRecord(TransactionContext transactionContext, CapitalTradeOrderDto tradeOrderDto) {
        LOG.debug("==>capital confirm record called");

        TradeOrder tradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());
        tradeOrder.confirm();
        tradeOrderRepository.update(tradeOrder);
        CapitalAccount transferToAccount = capitalAccountRepository.findByUserId(tradeOrderDto.getOppositeUserId());
        // 增加收款方资金帐户资金
        transferToAccount.transferTo(tradeOrderDto.getAmount());
        capitalAccountRepository.save(transferToAccount);
    }

    @Transactional
    public void cancelRecord(TransactionContext transactionContext, CapitalTradeOrderDto tradeOrderDto) {
        LOG.debug("==>capital cancel record called");
        TradeOrder tradeOrder = tradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());
        if (null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.cancel();
            tradeOrderRepository.update(tradeOrder);
            CapitalAccount capitalAccount = capitalAccountRepository.findByUserId(tradeOrderDto.getSelfUserId());
            // 对扣减付款方资金帐户资金进行反操作
            capitalAccount.cancelTransfer(tradeOrderDto.getAmount());

            capitalAccountRepository.save(capitalAccount);
        }
    }
}