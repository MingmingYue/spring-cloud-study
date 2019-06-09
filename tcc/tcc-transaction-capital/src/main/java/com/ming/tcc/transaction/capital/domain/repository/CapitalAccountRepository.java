package com.ming.tcc.transaction.capital.domain.repository;

import com.ming.tcc.transaction.capital.domain.entity.CapitalAccount;
import com.ming.tcc.transaction.capital.infrastruture.dao.CapitalAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Create on 2019-06-09.
 */
@Repository
public class CapitalAccountRepository {

    @Autowired
    CapitalAccountDao capitalAccountDao;

    public CapitalAccount findByUserId(long userId) {

        return capitalAccountDao.findByUserId(userId);
    }

    public void save(CapitalAccount capitalAccount) {
        capitalAccountDao.update(capitalAccount);
    }
}