package com.ming.tcc.transaction.redpacket.service;

import java.math.BigDecimal;

/**
 * Create on 2019-06-09.
 */
public interface RedPacketAccountService {
    BigDecimal getRedPacketAccountByUserId(long userId);
}