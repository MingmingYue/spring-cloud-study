package com.ming.tcc.transaction.redpacket.infrastructure.dao;

import com.ming.tcc.transaction.redpacket.domain.entity.RedPacketAccount;

/**
 * Create on 2019-06-09.
 */
public interface RedPacketAccountDao {

    RedPacketAccount findByUserId(long userId);

    void update(RedPacketAccount redPacketAccount);
}
