package com.ming.tcc.transaction.capital.service;

import com.ming.tcc.transaction.capital.dto.CapitalTradeOrderDto;
import com.ming.tcc.transation.api.TransactionContext;

/**
 * Create on 2019-06-09.
 */
public interface CapitalTradeOrderService {

    /**
     * 创建资金帐户变更记录.
     */
    String record(TransactionContext transactionContext, CapitalTradeOrderDto tradeOrderDto);
}
