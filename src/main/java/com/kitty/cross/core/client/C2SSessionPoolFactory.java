package com.kitty.cross.core.client;

import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cxf.send.service.GameSendService;
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
        (this.config = new GenericObjectPoolConfig<>()).setMaxTotal(Runtime.getRuntime().availableProcessors());
        this.config.setMaxWaitMillis(5000L);
        C2SSessionPoolFactory.self = this;
        // 这里尝试连接后端，连接不成功服务器退出
//        SpringUtils.getBean(GameSendService.class).send();
    }
    
    public static C2SSessionPoolFactory getInstance() {
        return C2SSessionPoolFactory.self;
    }
    
    public CCSession borrowSession(String ip, int port) {
        String key = this.buildKey(ip, port);
        try {
            C2SSessionFactory factory = new C2SSessionFactory(ip, port);
            GenericObjectPool<CCSession> pool = (GenericObjectPool)this.pools.getOrDefault(key, new GenericObjectPool(factory, this.config));
            this.pools.putIfAbsent(key, pool);
            return pool.borrowObject();
        }
        catch (Exception e) {
            LoggerUtils.error("", e);
            return null;
        }
    }
    
    public CCSession borrowCrossSession() {
        CrossServerConfig config = SpringUtils.getBean(CrossServerConfig.class);
        String ip = config.getCenterIp();
        int port = config.getCenterPort();
        return this.borrowSession(ip, port);
    }
    
    public void returnSession(CCSession session) {
        String key = this.buildKey(session.getIpAddr(), session.getPort());
        GenericObjectPool<CCSession> pool = this.pools.get(key);
        if (pool != null) {
            pool.returnObject(session);
        }
    }
    
    private String buildKey(String ip, int port) {
        return ip + "-" + port;
    }
}
