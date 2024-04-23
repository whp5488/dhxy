package com.kitty.cross.core.client;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;

@Component
public class C2SSessionPoolFactory {
  private GenericObjectPoolConfig config;
  
  private ConcurrentMap<String, GenericObjectPool<CCSession>> pools = new ConcurrentHashMap<>();
  
  private static volatile C2SSessionPoolFactory self;
  
  @PostConstruct
  private void init() {
    this.config = new GenericObjectPoolConfig();
    this.config.setMaxTotal(Runtime.getRuntime().availableProcessors());
    this.config.setMaxWaitMillis(5000L);
    self = this;
  }
  
  public static C2SSessionPoolFactory getInstance() {
    return self;
  }
  
  public CCSession borrowSession(String ip, int port) {
    String key = buildKey(ip, port);
    try {
      C2SSessionFactory factory = new C2SSessionFactory(ip, port);
      GenericObjectPool<CCSession> pool = this.pools.getOrDefault(key, new GenericObjectPool((PooledObjectFactory)factory, this.config));
      this.pools.putIfAbsent(key, pool);
      return (CCSession)pool.borrowObject();
    } catch (Exception e) {
      LoggerUtils.error("", e);
      return null;
    } 
  }
  
  public CCSession borrowCrossSession() {
    CrossServerConfig config = (CrossServerConfig)SpringUtils.getBean(CrossServerConfig.class);
    String ip = config.getCenterIp();
    int port = config.getCenterPort();
    return borrowSession(ip, port);
  }
  
  public void returnSession(CCSession session) {
    String key = buildKey(session.getIpAddr(), session.getPort());
    GenericObjectPool<CCSession> pool = this.pools.get(key);
    if (pool != null)
      pool.returnObject(session); 
  }
  
  private String buildKey(String ip, int port) {
    return ip + "-" + port;
  }
}
