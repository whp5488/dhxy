package com.kitty.common.thread;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.ServerConfig;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator
{
    private static AtomicLong generator;
    
    public IdGenerator() {
    }
    
    public static long getNextId() {
        ServerConfig config = (ServerConfig)SpringUtils.getBean(ServerConfig.class);
        long serverId = (long)config.getSerial();
        return serverId << 48 | (System.currentTimeMillis() / 1000L & -1L) << 16 | (IdGenerator.generator.getAndIncrement() & 0xFFFFL);
    }
    
    static {
        IdGenerator.generator = new AtomicLong(0L);
    }
}
