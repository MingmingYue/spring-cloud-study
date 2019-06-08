package com.ming.tcc.transaction.core.repository;

import com.ming.tcc.transaction.core.Transaction;
import com.ming.tcc.transaction.core.common.TransactionType;
import com.ming.tcc.transaction.core.exception.TransactionIOException;
import com.ming.tcc.transaction.core.repository.helper.RedisHelper;
import com.ming.tcc.transaction.core.repository.helper.TransactionSerializer;
import com.ming.tcc.transaction.core.serializer.JdkSerializationSerializer;
import com.ming.tcc.transaction.core.serializer.ObjectSerializer;
import com.ming.tcc.transaction.core.utils.ByteUtil;
import redis.clients.jedis.JedisPool;


import javax.transaction.xa.Xid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Redis缓存事务库.
 * Create on 2019-06-08.
 */
public class RedisTransactionRepository extends CachableTransactionRepository {

    private JedisPool jedisPool;

    private String keyPrefix = "TCC:";

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    private ObjectSerializer serializer = new JdkSerializationSerializer();

    public void setSerializer(ObjectSerializer serializer) {
        this.serializer = serializer;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    protected int doCreate(final Transaction transaction) {
        try {
            final byte[] key = RedisHelper.getRedisKey(keyPrefix, transaction.getXid());
            Long statusCode = RedisHelper.execute(jedisPool, (jedis) ->
                    jedis.hsetnx(key, ByteUtil.longToBytes(transaction.getVersion()), TransactionSerializer.serialize(serializer, transaction)));
            return statusCode.intValue();
        } catch (Exception e) {
            throw new TransactionIOException(e);
        }
    }

    @Override
    protected int doUpdate(final Transaction transaction) {
        try {
            final byte[] key = RedisHelper.getRedisKey(keyPrefix, transaction.getXid());
            Long statusCode = RedisHelper.execute(jedisPool, (jedis) -> {
                transaction.updateTime();
                transaction.updateVersion();
                return jedis.hsetnx(key, ByteUtil.longToBytes(transaction.getVersion()), TransactionSerializer.serialize(serializer, transaction));
            });
            return statusCode.intValue();
        } catch (Exception e) {
            throw new TransactionIOException(e);
        }
    }

    @Override
    protected int doDelete(Transaction transaction) {
        try {
            final byte[] key = RedisHelper.getRedisKey(keyPrefix, transaction.getXid());
            Long result = RedisHelper.execute(jedisPool, (jedis) -> jedis.del(key));
            return result.intValue();
        } catch (Exception e) {
            throw new TransactionIOException(e);
        }
    }

    @Override
    protected Transaction doFindOne(Xid xid) {
        try {
            final byte[] key = RedisHelper.getRedisKey(keyPrefix, xid);
            byte[] content = RedisHelper.getKeyValue(jedisPool, key);

            if (content != null) {
                return TransactionSerializer.deserialize(serializer, content);
            }
            return null;
        } catch (Exception e) {
            throw new TransactionIOException(e);
        }
    }

    @Override
    protected List<Transaction> doFindAllUnmodifiedSince(Date date) {
        List<Transaction> allTransactions = doFindAll();
        List<Transaction> allUnmodifiedSince = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getTransactionType().equals(TransactionType.ROOT)
                    && transaction.getLastUpdateTime().compareTo(date) < 0) {
                allUnmodifiedSince.add(transaction);
            }
        }
        return allUnmodifiedSince;
    }

    protected List<Transaction> doFindAll() {
        try {
            List<Transaction> transactions = new ArrayList<>();
            Set<byte[]> keys = RedisHelper.execute(jedisPool, (jedis) -> jedis.keys((keyPrefix + "*").getBytes()));
            for (final byte[] key : keys) {
                byte[] content = RedisHelper.getKeyValue(jedisPool, key);
                if (content != null) {
                    transactions.add(TransactionSerializer.deserialize(serializer, content));
                }
            }
            return transactions;
        } catch (Exception e) {
            throw new TransactionIOException(e);
        }
    }
}
