package com.kitty.common.core;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.logs.LoggerUtils;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

public class SchedulerManager {
  private static SchedulerManager instance;
  
  private ScheduledExecutorService service;
  
  public static SchedulerManager getInstance() {
    if (instance != null)
      return instance; 
    synchronized (SchedulerManager.class) {
      if (instance == null) {
        instance = new SchedulerManager();
        instance.init();
      } 
    } 
    return instance;
  }
  
  @PostConstruct
  private void init() {
    this.service = Executors.newScheduledThreadPool(2, (ThreadFactory)new NamedThreadFactory("common-scheduler"));
  }
  
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period) {
    return this.service.scheduleAtFixedRate(new LogTask(command), initialDelay, period, TimeUnit.MILLISECONDS);
  }
  
  public ScheduledFuture<?> schedule(Runnable command, long delay) {
    return this.service.schedule(new LogTask(command), delay, TimeUnit.MILLISECONDS);
  }
  
  public void shutDown() {
    this.service.shutdown();
    this.service.shutdownNow();
    LoggerUtils.error("定时器关闭结束", new Object[0]);
  }
  
  private static class LogTask implements Runnable {
    Runnable wrapper;
    
    public LogTask(Runnable wrapper) {
      this.wrapper = wrapper;
    }
    
    public void run() {
      try {
        this.wrapper.run();
      } catch (Throwable e) {
        e.printStackTrace();
        LoggerUtils.error("定时任务执行异常", new Object[] { e.getMessage() });
      } 
    }
  }
}
