package com.kitty.common.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
  private static final Logger logger = LoggerFactory.getLogger(VisiableThreadPoolTaskExecutor.class);
  
  private void showThreadPoolInfo(String prefix) {
    ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
    if (null == threadPoolExecutor)
      return; 
    logger.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]", getThreadNamePrefix(), prefix,
            threadPoolExecutor.getTaskCount(),
            threadPoolExecutor.getCompletedTaskCount(),
            threadPoolExecutor.getActiveCount(),
            threadPoolExecutor.getQueue().size());
  }
  
  public void execute(Runnable task) {
    showThreadPoolInfo("1. do execute");
    super.execute(task);
  }
  
  public void execute(Runnable task, long startTimeout) {
    showThreadPoolInfo("2. do execute");
    super.execute(task, startTimeout);
  }
  
  public Future<?> submit(Runnable task) {
    showThreadPoolInfo("1. do submit");
    return super.submit(task);
  }
  
  public <T> Future<T> submit(Callable<T> task) {
    showThreadPoolInfo("2. do submit");
    return super.submit(task);
  }
  
  public ListenableFuture<?> submitListenable(Runnable task) {
    showThreadPoolInfo("1. do submitListenable");
    return super.submitListenable(task);
  }
  
  public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
    showThreadPoolInfo("2. do submitListenable");
    return super.submitListenable(task);
  }
}
