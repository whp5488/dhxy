package com.kitty.cross.core;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.cross.core.client.CCSession;
import com.kitty.cross.core.server.SCSession;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class CrossCmdExecutor {
  private static volatile CrossCmdExecutor self;
  
  private final int DEFAULT_CORE_SUM = Runtime.getRuntime().availableProcessors() - 2;
  
  private ExecutorService[] services;
  
  public static CrossCmdExecutor getInstance() {
    if (self != null)
      return self; 
    synchronized (CrossCmdExecutor.class) {
      if (self == null) {
        CrossCmdExecutor instance = new CrossCmdExecutor();
        instance.init();
        self = instance;
      } 
    } 
    return self;
  }
  
  private void init() {
    this.services = new ExecutorService[this.DEFAULT_CORE_SUM];
    for (int i = 0; i < this.DEFAULT_CORE_SUM; i++)
      this.services[i] = Executors.newSingleThreadExecutor((ThreadFactory)new NamedThreadFactory("cross-service-" + i)); 
  }
  
  public void addTask(SCSession session, Runnable task) {
    int sessionId = session.getId();
    int index = sessionId % this.services.length;
    this.services[index].submit(task);
  }
  
  public void addTask(CCSession session, Runnable task) {
    int sessionId = session.getId();
    int index = sessionId % this.services.length;
    this.services[index].submit(task);
  }
  
  public void shutDown() {
    for (int i = 0; i < this.DEFAULT_CORE_SUM; i++)
      this.services[i].shutdown(); 
  }
}
