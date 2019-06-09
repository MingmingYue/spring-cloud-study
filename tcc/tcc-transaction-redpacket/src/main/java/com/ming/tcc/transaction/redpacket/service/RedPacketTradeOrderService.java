package com.ming.tcc.transaction.redpacket.service;

import com.ming.tcc.transaction.redpacket.dto.RedPacketTradeOrderDto;
import com.ming.tcc.transation.api.TransactionContext;

/**
 * Create on 2019-06-09.
 */
public interface RedPacketTradeOrderService {

    String record(TransactionContext transactionContext, RedPacketTradeOrderDto tradeOrderDto);
}