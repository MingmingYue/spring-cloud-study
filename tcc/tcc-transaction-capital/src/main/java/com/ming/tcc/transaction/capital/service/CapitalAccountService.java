package com.ming.tcc.transaction.capital.service;

import java.math.BigDecimal;

/**
 * Create on 2019-06-09.
 */
public interface CapitalAccountService {

    BigDecimal getCapitalAccountByUserId(long userId);
}
