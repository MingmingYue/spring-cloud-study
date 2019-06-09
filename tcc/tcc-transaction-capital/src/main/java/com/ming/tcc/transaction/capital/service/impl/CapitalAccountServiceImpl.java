package com.ming.tcc.transaction.capital.service.impl;

import com.ming.tcc.transaction.capital.domain.repository.CapitalAccountRepository;
import com.ming.tcc.transaction.capital.service.CapitalAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Create on 2019-06-09.
 */
@Service("capitalAccountService")
public class CapitalAccountServiceImpl implements CapitalAccountService {
    
    @Autowired
    CapitalAccountRepository capitalAccountRepository;

    @Override
    public BigDecimal getCapitalAccountByUserId(long userId) {
        return capitalAccountRepository.findByUserId(userId).getBalanceAmount();
    }
}