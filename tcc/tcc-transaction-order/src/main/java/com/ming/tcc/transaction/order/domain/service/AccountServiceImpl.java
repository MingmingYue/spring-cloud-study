package com.ming.tcc.transaction.order.domain.service;

import com.ming.tcc.transaction.capital.service.CapitalAccountService;
import com.ming.tcc.transaction.redpacket.service.RedPacketAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Create on 2019-06-09.
 */
@Service("accountService")
public class AccountServiceImpl {

    @Autowired
    RedPacketAccountService redPacketAccountService;

    @Autowired
    CapitalAccountService capitalAccountService;


    public BigDecimal getRedPacketAccountByUserId(long userId){
        return redPacketAccountService.getRedPacketAccountByUserId(userId);
    }

    public BigDecimal getCapitalAccountByUserId(long userId){
        return capitalAccountService.getCapitalAccountByUserId(userId);
    }
}