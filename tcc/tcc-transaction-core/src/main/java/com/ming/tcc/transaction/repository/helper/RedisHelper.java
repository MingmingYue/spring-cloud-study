package com.ming.tcc.transaction.repository.helper;

import com.ming.tcc.transaction.utils.ByteUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.transaction.xa.Xid;
import java.util.*;

/**
 * Create on 2019-06-08.
 */
public class RedisHelper {

    public static byte[] getRedisKey(String keyPrefix, Xid xid) {
        byte[] prefix = keyPrefix.getBytes();
        byte[] globalTransactionId = xid.getGlobalTransactionId();
        byte[] branchQualifier = xid.getBranchQualifier();

        byte[] key = new byte[prefix.length + globalTransactionId.length + branchQualifier.length];
        System.arraycopy(prefix, 0, key, 0, prefix.length);
        System.arraycopy(globalTransactionId, 0, key, prefix.length, globalTransactionId.length);
        System.arraycopy(branchQualifier, 0, key, prefix.length + globalTransactionId.length, branchQualifier.length);
        return key;
    }

    public static byte[] getKeyValue(JedisPool jedisPool, final byte[] key) {
        return execute(jedisPool, (jedis) -> getKeyValue(jedis, key));
    }

    public static byte[] getKeyValue(Jedis jedis, final byte[] key) {
        Map<byte[], byte[]> fieldValueMap = jedis.hgetAll(key);

        List<Map.Entry<byte[], byte[]>> entries = new ArrayList<>(fieldValueMap.entrySet());
        Collections.sort(entries, (entry1, entry2) -> (int) (ByteUtil.bytesToLong(entry1.getKey()) - ByteUtil.bytesToLong(entry2.getKey())));
        byte[] content = entries.get(entries.size() - 1).getValue();
        return content;
    }

    public static <T> T execute(JedisPool jedisPool, JedisCallback<T> callback) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return callback.doInJedis(jedis);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
