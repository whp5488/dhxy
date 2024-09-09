package com.kitty.cache;

import java.util.Iterator;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisPool;
import org.springframework.stereotype.Component;

@Component
public class JedisUtil
{
    private static JedisPool jedisPool;
    private static String redisHost;
    private static int redisPort;
    private static String redisPassword;
    private static int redisDatabase;
    private static int redisTimeout;
    
    public JedisUtil() {
    }
    
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
        if (JedisUtil.redisPassword != null && JedisUtil.redisPassword.length() > 1) {
            JedisUtil.jedisPool = new JedisPool(jedisPoolConfig, JedisUtil.redisHost, JedisUtil.redisPort, JedisUtil.redisTimeout, JedisUtil.redisPassword);
        }
        else {
            JedisUtil.jedisPool = new JedisPool(jedisPoolConfig, JedisUtil.redisHost, JedisUtil.redisPort);
        }
    }
    
    public static Jedis getJedis() {
        Jedis jedis = JedisUtil.jedisPool.getResource();
        jedis.select(JedisUtil.redisDatabase);
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
        return (long)jedis.del(key);
    }
    
    public static void delContain(String key) {
        Jedis jedis = getJedis();
        Set<String> keys = jedis.keys(key);
        for (String s : keys) {
            jedis.del(s);
        }
    }
    
    public static boolean contain(String key) {
        Set<String> keys = keys(key);
        return keys.size() > 0;
    }
    
    static {
        JedisUtil.redisHost = "127.0.0.1";
        JedisUtil.redisPort = 6379;
        JedisUtil.redisDatabase = 0;
        JedisUtil.redisTimeout = 5000;
    }
}
