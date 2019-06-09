package com.ming.tcc.transaction.redpacket.service.impl;

import com.ming.tcc.transaction.redpacket.domain.repository.RedPacketAccountRepository;
import com.ming.tcc.transaction.redpacket.service.RedPacketAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Create on 2019-06-09.
 */
@Service("redPacketAccountService")
public class RedPacketAccountServiceImpl implements RedPacketAccountService {

    @Autowired
    RedPacketAccountRepository redPacketAccountRepository;

    @Override
    public BigDecimal getRedPacketAccountByUserId(long userId) {
        return redPacketAccountRepository.findByUserId(userId).getBalanceAmount();
    }
}