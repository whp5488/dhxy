
package com.kitty.cache;

import java.util.HashMap;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.connection.RedisZSetCommands;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import javax.annotation.Resource;
import java.io.Serializable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService
{
    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;
    
    public void cutLink(Integer index) {
        if (index == null || index > 15 || index < 0) {
            index = 0;
        }
        final LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory)this.redisTemplate.getConnectionFactory();
        if (connectionFactory != null && index != connectionFactory.getDatabase()) {
            connectionFactory.setDatabase((int)index);
            this.redisTemplate.setConnectionFactory((RedisConnectionFactory)connectionFactory);
            connectionFactory.setShareNativeConnection(false);
            connectionFactory.afterPropertiesSet();
        }
    }
    
    public void sendChannelMessage(final String channel, final Serializable message) {
        this.redisTemplate.convertAndSend(channel, message);
    }
    
    public Boolean exists(final Serializable key) {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                return connection.exists(RedisSerializer.serialize(key));
            }
        });
    }
    
    public Boolean flushDB() {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return true;
            }
        });
    }
    
    public Long del(final Serializable... keys) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[][] ks = new byte[keys.length][];
                for (int i = 0; i < keys.length; ++i) {
                    ks[i] = RedisSerializer.serialize(keys[i]);
                }
                return connection.del(ks);
            }
        });
    }
    
    public Boolean expire(final Serializable key, final long seconds) {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                return connection.expire(RedisSerializer.serialize(key), seconds);
            }
        });
    }
    
    public Boolean persist(final Serializable key) {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                return connection.persist(RedisSerializer.serialize(key));
            }
        });
    }
    
    public Long ttl(final Serializable key) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                return connection.ttl(RedisSerializer.serialize(key));
            }
        });
    }
    
    public void set(final Serializable key, final Serializable value, final long seconds) {
        this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] v = RedisSerializer.serialize(value);
                connection.set(k, v);
                if (seconds > 0L) {
                    connection.expire(RedisSerializer.serialize(key), seconds);
                }
                return true;
            }
        });
    }
    
    public <T extends Serializable> T get(final Serializable key, final Class<T> clazz) {
        return (T)this.redisTemplate.execute((RedisCallback)new RedisCallback<T>() {
            public T doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] result = connection.get(k);
                return RedisSerializer.deserialize(result, clazz);
            }
        });
    }
    
    public String getSet(final String key, final String value) {
        return (String)this.redisTemplate.execute((RedisCallback)new RedisCallback<String>() {
            public String doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] v = RedisSerializer.serialize(value);
                final byte[] result = connection.getSet(k, v);
                return RedisSerializer.deserialize(result, String.class);
            }
        });
    }
    
    public Long remove(final String key) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                return connection.del(new byte[][] { k });
            }
        });
    }
    
    public Long incr(final Serializable key) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                return connection.incr(k);
            }
        });
    }
    
    public Long decr(final Serializable key) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                return connection.decr(k);
            }
        });
    }
    
    public Long sAdd(final Serializable key, final Serializable... values) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[][] vs = new byte[values.length][];
                for (int i = 0; i < values.length; ++i) {
                    vs[i] = RedisSerializer.serialize(values[i]);
                }
                return connection.sAdd(k, vs);
            }
        });
    }
    
    public Long sCard(final Serializable key) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                return connection.sCard(k);
            }
        });
    }
    
    public Boolean sIsMember(final Serializable key, final Serializable value) {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] v = RedisSerializer.serialize(value);
                return connection.sIsMember(k, v);
            }
        });
    }
    
    public <T extends Serializable> Set<T> sMembers(final Serializable key, final Class<T> clazz) {
        return (Set)this.redisTemplate.execute((RedisCallback)new RedisCallback<Set<T>>() {
            public Set<T> doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final Set<byte[]> sets = connection.sMembers(k);
                final Set<T> result = new HashSet<T>();
                if (sets != null && sets.size() > 0) {
                    for (final byte[] bs : sets) {
                        result.add(RedisSerializer.deserialize(bs, clazz));
                    }
                }
                return result;
            }
        });
    }
    
    public <T extends Serializable> LinkedHashMap<T, Double> zReverseRangeWithScore(final Serializable key, final Class<T> clazz, final int start, final int end) {
        return (LinkedHashMap)this.redisTemplate.execute((RedisCallback)new RedisCallback<LinkedHashMap<T, Double>>() {
            public LinkedHashMap<T, Double> doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final Set<RedisZSetCommands.Tuple> tuples = connection.zRangeWithScores(k, (long)start, (long)end);
                final LinkedHashMap<T, Double> result = new LinkedHashMap<T, Double>();
                if (tuples != null && tuples.size() > 0) {
                    for (final RedisZSetCommands.Tuple tuple : tuples) {
                        result.put(RedisSerializer.deserialize(tuple.getValue(), clazz), tuple.getScore());
                    }
                }
                return result;
            }
        });
    }
    
    public Long sRem(final Serializable key, final Serializable... values) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[][] vs = new byte[values.length][];
                for (int i = 0; i < values.length; ++i) {
                    vs[i] = RedisSerializer.serialize(values[i]);
                }
                return connection.sRem(k, vs);
            }
        });
    }
    
    public Boolean zAdd(final Serializable key, final double score, final Serializable value) {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] v = RedisSerializer.serialize(value);
                return connection.zAdd(k, score, v);
            }
        });
    }
    
    public Long zCard(final Serializable key) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                return connection.zCard(k);
            }
        });
    }
    
    public Long zRem(final Serializable key, final Serializable... values) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[][] vs = new byte[values.length][];
                for (int i = 0; i < values.length; ++i) {
                    vs[i] = RedisSerializer.serialize(values[i]);
                }
                return connection.zRem(k, vs);
            }
        });
    }
    
    public Boolean hSet(final Serializable key, final Serializable field, final Serializable value) {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] f = RedisSerializer.serialize(field);
                final byte[] v = RedisSerializer.serialize(value);
                return connection.hSet(k, f, v);
            }
        });
    }
    
    public Long hDel(final Serializable key, final Serializable... fields) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[][] fs = new byte[fields.length][];
                for (int i = 0; i < fields.length; ++i) {
                    fs[i] = RedisSerializer.serialize(fields[i]);
                }
                return connection.hDel(k, fs);
            }
        });
    }
    
    public Boolean hExists(final Serializable key, final Serializable field) {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] f = RedisSerializer.serialize(field);
                return connection.hExists(k, f);
            }
        });
    }
    
    public <T extends Serializable> T hGet(final Serializable key, final Serializable field, final Class<T> clazz) {
        return (T)this.redisTemplate.execute((RedisCallback)new RedisCallback<T>() {
            public T doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] f = RedisSerializer.serialize(field);
                final byte[] result = connection.hGet(k, f);
                return RedisSerializer.deserialize(result, clazz);
            }
        });
    }
    
    public <T extends Serializable> Set<T> hKeys(final Serializable key, final Class<T> clazz) {
        return (Set)this.redisTemplate.execute((RedisCallback)new RedisCallback<Set<T>>() {
            public Set<T> doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final Set<byte[]> result = connection.hKeys(k);
                final Set<T> sets = new HashSet<T>();
                if (result != null && result.size() > 0) {
                    for (final byte[] b : result) {
                        final T t = RedisSerializer.deserialize(b, clazz);
                        sets.add(t);
                    }
                }
                return sets;
            }
        });
    }
    
    public <T extends Serializable> List<T> hVals(final Serializable key, final Class<T> clazz) {
        return (List)this.redisTemplate.execute((RedisCallback)new RedisCallback<List<T>>() {
            public List<T> doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final List<byte[]> result = connection.hVals(k);
                final List<T> sets = new ArrayList<T>();
                if (result != null && result.size() > 0) {
                    for (final byte[] b : result) {
                        final T t = RedisSerializer.deserialize(b, clazz);
                        sets.add(t);
                    }
                }
                return sets;
            }
        });
    }
    
    public <K extends Serializable, V extends Serializable> Map<K, V> hGetAll(final Serializable key, final Class<K> innerKey, final Class<V> valueClass) {
        return (Map)this.redisTemplate.execute((RedisCallback<Map<Object, Object>>) connection -> {
            final byte[] keyBytes = RedisSerializer.serialize(key);
            final Map<byte[], byte[]> map = connection.hGetAll(keyBytes);
            final Map<Object, Object> resultMap = new HashMap<>(8);
            if (map != null) {
                map.forEach((k, v) -> {
                    resultMap.put(RedisSerializer.deserialize(k, innerKey), RedisSerializer.deserialize(v, valueClass));
                });
            }
            return resultMap;
        });
    }
    
    public Long lLen(final Serializable key) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                return connection.lLen(k);
            }
        });
    }
    
    public Boolean lSet(final Serializable key, final int index, final Serializable value) {
        return (Boolean)this.redisTemplate.execute((RedisCallback)new RedisCallback<Boolean>() {
            public Boolean doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] v = RedisSerializer.serialize(value);
                connection.lSet(k, (long)index, v);
                return true;
            }
        });
    }
    
    public <T extends Serializable> T lIndex(final Serializable key, final int index, final Class<T> clazz) {
        return (T)this.redisTemplate.execute((RedisCallback)new RedisCallback<T>() {
            public T doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] result = connection.lIndex(k, (long)index);
                return RedisSerializer.deserialize(result, clazz);
            }
        });
    }
    
    public <T extends Serializable> T lPop(final Serializable key, final Class<T> clazz) {
        return (T)this.redisTemplate.execute((RedisCallback)new RedisCallback<T>() {
            public T doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] result = connection.lPop(k);
                return RedisSerializer.deserialize(result, clazz);
            }
        });
    }
    
    public <T extends Serializable> T rPop(final Serializable key, final Class<T> clazz) {
        return (T)this.redisTemplate.execute((RedisCallback)new RedisCallback<T>() {
            public T doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] result = connection.rPop(k);
                return RedisSerializer.deserialize(result, clazz);
            }
        });
    }
    
    public <T extends Serializable> T bRPop(final Serializable key, final Class<T> clazz) {
        return (T)this.redisTemplate.execute((RedisCallback<Serializable>) connection -> {
            final byte[] k = RedisSerializer.serialize(key);
            final List<byte[]> bytes = connection.bRPop(60, new byte[][] { k });
            return RedisSerializer.deserialize((byte[])((bytes == null) ? null : ((byte[])bytes.get(1))), (Class<Serializable>)clazz);
        });
    }
    
    public Long lPush(final Serializable key, final Serializable... values) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[][] vs = new byte[values.length][];
                for (int i = 0; i < values.length; ++i) {
                    vs[i] = RedisSerializer.serialize(values[i]);
                }
                return connection.lPush(k, vs);
            }
        });
    }
    
    public Long rPush(final Serializable key, final Serializable... values) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[][] vs = new byte[values.length][];
                for (int i = 0; i < values.length; ++i) {
                    vs[i] = RedisSerializer.serialize(values[i]);
                }
                return connection.rPush(k, vs);
            }
        });
    }
    
    public <T extends Serializable> List<T> lrange(final Serializable key, final int start, final int end, final Class<T> clazz) {
        return (List)this.redisTemplate.execute((RedisCallback)new RedisCallback<List<T>>() {
            public List<T> doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final List<byte[]> list = connection.lRange(k, (long)start, (long)end);
                final List<T> result = new ArrayList<T>();
                if (list != null && list.size() > 0) {
                    for (final byte[] bs : list) {
                        result.add(RedisSerializer.deserialize(bs, clazz));
                    }
                }
                return result;
            }
        });
    }
    
    public Long lRem(final Serializable key, final int count, final Serializable value) {
        return (Long)this.redisTemplate.execute((RedisCallback)new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection) throws DataAccessException {
                final byte[] k = RedisSerializer.serialize(key);
                final byte[] v = RedisSerializer.serialize(value);
                return connection.lRem(k, (long)count, v);
            }
        });
    }
    
    public Boolean setNx(final Serializable key, final Serializable value) {
        return (Boolean)this.redisTemplate.execute((RedisCallback<Object>) connection -> {
            final byte[] k = RedisSerializer.serialize(key);
            final byte[] v = RedisSerializer.serialize(value);
            return connection.setNX(k, v);
        });
    }
    
    public <T extends Serializable> List<T> getContain(final Serializable key, final Class<T> clazz) {
        final Set<Serializable> keys = this.redisTemplate.keys(key);
        final List<T> list = new ArrayList<T>();
        if (keys.size() > 0) {
            for (final Serializable k : keys) {
                final T t = this.get(k, clazz);
                list.add(t);
            }
        }
        return list;
    }
    
    public void delContain(final Serializable key) {
        final Set<Serializable> keys = this.redisTemplate.keys(key);
        if (keys.size() > 0) {
            this.redisTemplate.delete(keys);
        }
    }
}
