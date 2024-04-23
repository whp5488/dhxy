package com.kitty.cache;

import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class JedisUtil {
  private static JedisPool jedisPool;
  
  private static String redisHost = "127.0.0.1";
  
  private static int redisPort = 6379;
  
  private static String redisPassword;
  
  private static int redisDatabase = 0;
  
  private static int redisTimeout = 5000;
  
  @Value("${sms.redisHost}")
  public void setRedisHost(String redisHost) {
    JedisUtil.redisHost = redisHost;
  }
  
  @Value("${sms.redisPort}")
  public void setRedisPort(int redisPort) {
    JedisUtil.redisPort = redisPort;
  }
  
  @Value("${sms.redisPassword}")
  public void setRedisPassword(String redisPassword) {
    JedisUtil.redisPassword = redisPassword;
  }
  
  @Value("${sms.redisDatabase}")
  public void setRedisDatabase(int redisDatabase) {
    JedisUtil.redisDatabase = redisDatabase;
  }
  
  @Value("${sms.redisTimeout}")
  public void setRedisTimeout(int redisTimeout) {
    JedisUtil.redisTimeout = redisTimeout;
  }
  
  public static void load() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(8);
    jedisPoolConfig.setMinIdle(1);
    jedisPoolConfig.setMaxTotal(3000);
    if (redisPassword != null && redisPassword.length() > 1) {
      jedisPool = new JedisPool(jedisPoolConfig, redisHost, redisPort, redisTimeout, redisPassword);
    } else {
      jedisPool = new JedisPool(jedisPoolConfig, redisHost, redisPort);
    } 
  }
  
  public static Jedis getJedis() {
    Jedis jedis = jedisPool.getResource();
    jedis.select(redisDatabase);
    return jedis;
  }
  
  public static String set(String key, String value) {
    Jedis jedis = getJedis();
    return jedis.set(key, value);
  }
  
  public static String set(String key, int seconds, String value) {
    Jedis jedis = getJedis();
    return jedis.setex(key, seconds, value);
  }
  
  public static Set<String> keys(String key) {
    Jedis jedis = getJedis();
    return jedis.keys(key);
  }
  
  public static String get(String key) {
    Jedis jedis = getJedis();
    return jedis.get(key);
  }
  
  public static long del(String key) {
    Jedis jedis = getJedis();
    return jedis.del(key);
  }
  
  public static void delContain(String key) {
    Jedis jedis = getJedis();
    Set<String> keys = jedis.keys(key);
    for (String s : keys)
      jedis.del(s); 
  }
  
  public static boolean contain(String key) {
    Set<String> keys = keys(key);
    if (keys.size() > 0)
      return true; 
    return false;
  }
}
