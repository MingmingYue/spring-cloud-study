package com.ming.tcc.transaction.capital.infrastruture.dao;

import com.ming.tcc.transaction.capital.domain.entity.CapitalAccount;

/**
 * Create on 2019-06-09.
 */
public interface CapitalAccountDao {

    CapitalAccount findByUserId(long userId);

    void update(CapitalAccount capitalAccount);
}