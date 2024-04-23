package com.kitty.common.thread;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.ServerConfig;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
  private static AtomicLong generator = new AtomicLong(0L);
  
  public static long getNextId() {
    ServerConfig config = (ServerConfig)SpringUtils.getBean(ServerConfig.class);
    long serverId = config.getSerial();
    return serverId << 48L | (
      System.currentTimeMillis() / 1000L & 0xFFFFFFFFFFFFFFFFL) << 16L | generator
      .getAndIncrement() & 0xFFFFL;
  }
}
