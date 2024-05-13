
package com.kitty.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

@Component
public class JedisUtil
{
    private static JedisPool jedisPool;
    private static String redisHost;
    private static int redisPort;
    private static String redisPassword;
    private static int redisDatabase;
    private static int redisTimeout;
    
    @Value("${sms.redisHost}")
    public void setRedisHost(final String redisHost) {
        JedisUtil.redisHost = redisHost;
    }
    
    @Value("${sms.redisPort}")
    public void setRedisPort(final int redisPort) {
        JedisUtil.redisPort = redisPort;
    }
    
    @Value("${sms.redisPassword}")
    public void setRedisPassword(final String redisPassword) {
        JedisUtil.redisPassword = redisPassword;
    }
    
    @Value("${sms.redisDatabase}")
    public void setRedisDatabase(final int redisDatabase) {
        JedisUtil.redisDatabase = redisDatabase;
    }
    
    @Value("${sms.redisTimeout}")
    public void setRedisTimeout(final int redisTimeout) {
        JedisUtil.redisTimeout = redisTimeout;
    }
    
    public static void load() {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
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
        final Jedis jedis = JedisUtil.jedisPool.getResource();
        jedis.select(JedisUtil.redisDatabase);
        return jedis;
    }
    
    public static String set(final String key, final String value) {
        final Jedis jedis = getJedis();
        return jedis.set(key, value);
    }
    
    public static String set(final String key, final int seconds, final String value) {
        final Jedis jedis = getJedis();
        return jedis.setex(key, seconds, value);
    }
    
    public static Set<String> keys(final String key) {
        final Jedis jedis = getJedis();
        return jedis.keys(key);
    }
    
    public static String get(final String key) {
        final Jedis jedis = getJedis();
        return jedis.get(key);
    }
    
    public static long del(final String key) {
        final Jedis jedis = getJedis();
        return jedis.del(key);
    }
    
    public static void delContain(final String key) {
        final Jedis jedis = getJedis();
        final Set<String> keys = jedis.keys(key);
        for (final String s : keys) {
            jedis.del(s);
        }
    }
    
    public static boolean contain(final String key) {
        final Set<String> keys = keys(key);
        return keys.size() > 0;
    }
    
    static {
        JedisUtil.redisHost = "127.0.0.1";
        JedisUtil.redisPort = 6379;
        JedisUtil.redisDatabase = 0;
        JedisUtil.redisTimeout = 5000;
    }
}
