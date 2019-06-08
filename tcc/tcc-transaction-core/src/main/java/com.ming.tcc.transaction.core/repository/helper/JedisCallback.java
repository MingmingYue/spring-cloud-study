package com.ming.tcc.transaction.core.repository.helper;

import redis.clients.jedis.Jedis;

/**
 * Create on 2019-06-08.
 */
public interface JedisCallback<T> {

    T doInJedis(Jedis jedis);
}
