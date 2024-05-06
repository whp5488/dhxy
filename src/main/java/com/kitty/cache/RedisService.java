package com.kitty.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService {
  @Resource
  private RedisTemplate<Serializable, Serializable> redisTemplate;
  
  public void cutLink(Integer index) {
    if (index == null || index > 15 || index < 0)
      index = 0;
    LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory)this.redisTemplate.getConnectionFactory();
    if (connectionFactory != null && index != connectionFactory.getDatabase()) {
      connectionFactory.setDatabase(index);
      this.redisTemplate.setConnectionFactory(connectionFactory);
      connectionFactory.setShareNativeConnection(false);
      connectionFactory.afterPropertiesSet();
    } 
  }
  
  public void sendChannelMessage(String channel, Serializable message) {
    this.redisTemplate.convertAndSend(channel, message);
  }
  
  public Boolean exists(final Serializable key) {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(RedisSerializer.serialize(key)));
  }
  
  public Boolean flushDB() {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      connection.flushDb();
      return Boolean.TRUE;
    });
  }
  
  public Long del(Serializable... keys) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[][] ks = new byte[keys.length][];
      for (int i = 0; i < keys.length; i++)
        ks[i] = RedisSerializer.serialize(keys[i]);
      return connection.del(ks);
    });
  }
  
  public Boolean expire(final Serializable key, final long seconds) {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.expire(RedisSerializer.serialize(key), seconds));
  }
  
  public Boolean persist(final Serializable key) {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.persist(RedisSerializer.serialize(key)));
  }
  
  public Long ttl(final Serializable key) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> connection.ttl(RedisSerializer.serialize(key)));
  }
  
  public void set(final Serializable key, final Serializable value, final long seconds) {
    this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] v = RedisSerializer.serialize(value);
      connection.set(k, v);
      if (seconds > 0L)
        connection.expire(RedisSerializer.serialize(key), seconds);
      return Boolean.TRUE;
    });
  }
  
  public <T extends Serializable> T get(final Serializable key, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<T>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] result = connection.get(k);
      return (T)RedisSerializer.deserialize(result, clazz);
    });
  }
  
  public String getSet(final String key, final String value) {
    return this.redisTemplate.execute((RedisCallback<String>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] v = RedisSerializer.serialize(value);
      byte[] result = connection.getSet(k, v);
      return RedisSerializer.deserialize(result, String.class);
    });
  }
  
  public Long remove(final String key) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      return connection.del(new byte[][] { k });
    });
  }
  
  public Long incr(final Serializable key) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      return connection.incr(k);
    });
  }
  
  public Long decr(final Serializable key) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      return connection.decr(k);
    });
  }
  
  public Long sAdd(final Serializable key, Serializable... values) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[][] vs = new byte[values.length][];
      for (int i = 0; i < values.length; i++)
        vs[i] = RedisSerializer.serialize(values[i]);
      return connection.sAdd(k, vs);
    });
  }
  
  public Long sCard(final Serializable key) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      return connection.sCard(k);
    });
  }
  
  public Boolean sIsMember(final Serializable key, final Serializable value) {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] v = RedisSerializer.serialize(value);
      return connection.sIsMember(k, v);
    });
  }
  
  public <T extends Serializable> Set<T> sMembers(final Serializable key, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<Set<T>>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      Set<byte[]> sets = connection.sMembers(k);
      Set<T> result = new HashSet<>();
      if (sets != null && sets.size() > 0)
        for (byte[] bs : sets)
          result.add((T)RedisSerializer.deserialize(bs, clazz));
      return result;
    });
  }
  
  public <T extends Serializable> LinkedHashMap<T, Double> zReverseRangeWithScore(final Serializable key, final Class<T> clazz, final int start, final int end) {
    return this.redisTemplate.execute((RedisCallback<LinkedHashMap<T, Double>>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      Set<RedisZSetCommands.Tuple> tuples = connection.zRangeWithScores(k, start, end);
      LinkedHashMap<T, Double> result = new LinkedHashMap<>();
      if (tuples != null && tuples.size() > 0)
        for (RedisZSetCommands.Tuple tuple : tuples)
          result.put((T)RedisSerializer.deserialize(tuple.getValue(), clazz), tuple.getScore());
      return result;
    });
  }
  
  public Long sRem(final Serializable key, Serializable... values) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[][] vs = new byte[values.length][];
      for (int i = 0; i < values.length; i++)
        vs[i] = RedisSerializer.serialize(values[i]);
      return connection.sRem(k, vs);
    });
  }
  
  public Boolean zAdd(final Serializable key, final double score, final Serializable value) {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] v = RedisSerializer.serialize(value);
      return connection.zAdd(k, score, v);
    });
  }
  
  public Long zCard(final Serializable key) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      return connection.zCard(k);
    });
  }
  
  public Long zRem(final Serializable key, Serializable... values) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[][] vs = new byte[values.length][];
      for (int i = 0; i < values.length; i++)
        vs[i] = RedisSerializer.serialize(values[i]);
      return connection.zRem(k, vs);
    });
  }
  
  public Boolean hSet(final Serializable key, final Serializable field, final Serializable value) {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] f = RedisSerializer.serialize(field);
      byte[] v = RedisSerializer.serialize(value);
      return connection.hSet(k, f, v);
    });
  }
  
  public Long hDel(final Serializable key, Serializable... fields) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[][] fs = new byte[fields.length][];
      for (int i = 0; i < fields.length; i++)
        fs[i] = RedisSerializer.serialize(fields[i]);
      return connection.hDel(k, fs);
    });
  }
  
  public Boolean hExists(final Serializable key, final Serializable field) {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] f = RedisSerializer.serialize(field);
      return connection.hExists(k, f);
    });
  }
  
  public <T extends Serializable> T hGet(final Serializable key, final Serializable field, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<T>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] f = RedisSerializer.serialize(field);
      byte[] result = connection.hGet(k, f);
      return (T)RedisSerializer.deserialize(result, clazz);
    });
  }
  
  public <T extends Serializable> Set<T> hKeys(final Serializable key, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<Set<T>>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      Set<byte[]> result = connection.hKeys(k);
      Set<T> sets = new HashSet<>();
      if (result != null && result.size() > 0)
        for (byte[] b : result) {
          T t = (T)RedisSerializer.deserialize(b, clazz);
          sets.add(t);
        }
      return sets;
    });
  }
  
  public <T extends Serializable> List<T> hVals(final Serializable key, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<List<T>>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      List<byte[]> result = connection.hVals(k);
      List<T> sets = new ArrayList<>();
      if (result != null && result.size() > 0)
        for (byte[] b : result) {
          T t = (T)RedisSerializer.deserialize(b, clazz);
          sets.add(t);
        }
      return sets;
    });
  }
  
  public <K extends Serializable, V extends Serializable> Map<K, V> hGetAll(Serializable key, Class<K> innerKey, Class<V> valueClass) {
    return this.redisTemplate.execute((RedisCallback<Map<K, V>>) connection -> {
          byte[] keyBytes = RedisSerializer.serialize(key);
          Map<byte[], byte[]> map = connection.hGetAll(keyBytes);
          Map<K, V> resultMap = new HashMap<>(8);
          if (map != null)
              map.forEach((k, v) -> {
                  resultMap.put(RedisSerializer.deserialize(k, innerKey), RedisSerializer.deserialize(v, valueClass));
              });
          return resultMap;
        });
  }
  
  public Long lLen(final Serializable key) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      return connection.lLen(k);
    });
  }
  
  public Boolean lSet(final Serializable key, final int index, final Serializable value) {
    return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] v = RedisSerializer.serialize(value);
      connection.lSet(k, index, v);
      return Boolean.valueOf(true);
    });
  }
  
  public <T extends Serializable> T lIndex(final Serializable key, final int index, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<T>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] result = connection.lIndex(k, index);
      return (T)RedisSerializer.deserialize(result, clazz);
    });
  }
  
  public <T extends Serializable> T lPop(final Serializable key, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<T>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] result = connection.lPop(k);
      return (T)RedisSerializer.deserialize(result, clazz);
    });
  }
  
  public <T extends Serializable> T rPop(final Serializable key, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<T>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] result = connection.rPop(k);
      return (T)RedisSerializer.deserialize(result, clazz);
    });
  }
  
  public <T extends Serializable> T bRPop(Serializable key, Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<T>) connection -> {
        byte[] k = RedisSerializer.serialize(key);
        List<byte[]> bytes = connection.bRPop(60, new byte[][] { k });
        return RedisSerializer.deserialize((bytes == null) ? null : bytes.get(1), clazz);
    });
  }
  
  public Long lPush(final Serializable key, Serializable... values) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[][] vs = new byte[values.length][];
      for (int i = 0; i < values.length; i++)
        vs[i] = RedisSerializer.serialize(values[i]);
      return connection.lPush(k, vs);
    });
  }
  
  public Long rPush(final Serializable key, Serializable... values) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[][] vs = new byte[values.length][];
      for (int i = 0; i < values.length; i++)
        vs[i] = RedisSerializer.serialize(values[i]);
      return connection.rPush(k, vs);
    });
  }
  
  public <T extends Serializable> List<T> lrange(final Serializable key, final int start, final int end, final Class<T> clazz) {
    return this.redisTemplate.execute((RedisCallback<List<T>>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      List<byte[]> list = connection.lRange(k, start, end);
      List<T> result = new ArrayList<>();
      if (list != null && list.size() > 0)
        for (byte[] bs : list)
          result.add((T)RedisSerializer.deserialize(bs, clazz));
      return result;
    });
  }
  
  public Long lRem(final Serializable key, final int count, final Serializable value) {
    return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
      byte[] k = RedisSerializer.serialize(key);
      byte[] v = RedisSerializer.serialize(value);
      return connection.lRem(k, count, v);
    });
  }
  
  public Boolean setNx(Serializable key, Serializable value) {
      return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
          byte[] k = RedisSerializer.serialize(key);
          byte[] v = RedisSerializer.serialize(value);
          return connection.setNX(k, v);
      });
  }
  
  public <T extends Serializable> List<T> getContain(Serializable key, Class<T> clazz) {
    Set<Serializable> keys = this.redisTemplate.keys(key);
    List<T> list = new ArrayList<>();
    if (keys.size() > 0)
      for (Serializable k : keys) {
        T t = get(k, clazz);
        list.add(t);
      }  
    return list;
  }
  
  public void delContain(Serializable key) {
    Set<Serializable> keys = this.redisTemplate.keys(key);
    if (keys.size() > 0)
      this.redisTemplate.delete(keys); 
  }
}
