
package com.kitty.common.thread;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.ServerConfig;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator
{
    private static AtomicLong generator;
    
    public static long getNextId() {
        final ServerConfig config = SpringUtils.getBean(ServerConfig.class);
        final long serverId = config.getSerial();
        return serverId << 48 | (System.currentTimeMillis() / 1000L & -1L) << 16 | (IdGenerator.generator.getAndIncrement() & 0xFFFFL);
    }
    
    static {
        IdGenerator.generator = new AtomicLong(0L);
    }
}
