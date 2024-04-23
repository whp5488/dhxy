package com.kitty.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
  private ThreadGroup threadGroup;
  
  private String groupName;
  
  private final boolean daemo;
  
  private AtomicInteger idGenerator = new AtomicInteger(1);
  
  public NamedThreadFactory(String group) {
    this(group, false);
  }
  
  public NamedThreadFactory(String group, boolean daemo) {
    this.groupName = group;
    this.daemo = daemo;
  }
  
  public Thread newThread(Runnable r) {
    String name = getNextThreadName();
    Thread ret = new Thread(this.threadGroup, r, name, 0L);
    ret.setDaemon(this.daemo);
    return ret;
  }
  
  private String getNextThreadName() {
    return this.groupName + "-thread-" + this.idGenerator.getAndIncrement();
  }
}
