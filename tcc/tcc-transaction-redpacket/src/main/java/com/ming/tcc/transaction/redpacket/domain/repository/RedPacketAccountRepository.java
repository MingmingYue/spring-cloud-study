package com.ming.tcc.transaction.redpacket.domain.repository;

import com.ming.tcc.transaction.redpacket.domain.entity.RedPacketAccount;
import com.ming.tcc.transaction.redpacket.infrastructure.dao.RedPacketAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Create on 2019-06-09.
 */
@Repository
public class RedPacketAccountRepository {

    @Autowired
    RedPacketAccountDao redPacketAccountDao;

    public RedPacketAccount findByUserId(long userId) {

        return redPacketAccountDao.findByUserId(userId);
    }

    public void save(RedPacketAccount redPacketAccount) {
        redPacketAccountDao.update(redPacketAccount);
    }
}