
package com.kitty.cross.core.client;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.pool2.impl.GenericObjectPool;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;

@Component
public class C2SSessionPoolFactory
{
    private GenericObjectPoolConfig config;
    private ConcurrentMap<String, GenericObjectPool<CCSession>> pools;
    private static volatile C2SSessionPoolFactory self;
    
    public C2SSessionPoolFactory() {
        this.pools = new ConcurrentHashMap<>();
    }
    
    @PostConstruct
    private void init() {
        (this.config = new GenericObjectPoolConfig()).setMaxTotal(Runtime.getRuntime().availableProcessors());
        this.config.setMaxWaitMillis(5000L);
        C2SSessionPoolFactory.self = this;
    }
    
    public static C2SSessionPoolFactory getInstance() {
        return C2SSessionPoolFactory.self;
    }
    
    public CCSession borrowSession(final String ip, final int port) {
        final String key = this.buildKey(ip, port);
        try {
            final C2SSessionFactory factory = new C2SSessionFactory(ip, port);
            final GenericObjectPool<CCSession> pool = this.pools.getOrDefault(key, (GenericObjectPool<CCSession>)new GenericObjectPool(factory, this.config));
            this.pools.putIfAbsent(key, pool);
            return (CCSession)pool.borrowObject();
        }
        catch (final Exception e) {
            LoggerUtils.error("", e);
            return null;
        }
    }
    
    public CCSession borrowCrossSession() {
        final CrossServerConfig config = SpringUtils.getBean(CrossServerConfig.class);
        final String ip = config.getCenterIp();
        final int port = config.getCenterPort();
        return this.borrowSession(ip, port);
    }
    
    public void returnSession(final CCSession session) {
        final String key = this.buildKey(session.getIpAddr(), session.getPort());
        final GenericObjectPool<CCSession> pool = this.pools.get(key);
        if (pool != null) {
            pool.returnObject(session);
        }
    }
    
    private String buildKey(final String ip, final int port) {
        return ip + "-" + port;
    }
}
