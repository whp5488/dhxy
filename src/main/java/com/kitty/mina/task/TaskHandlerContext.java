package com.kitty.mina.task;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.JsonUtils;
import com.kitty.logs.LoggerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum TaskHandlerContext {
  INSTANCE;
  
  private final AtomicBoolean run;
  
  private final List<TaskWorker> workerPool;
  
  private final int CORE_SIZE;
  
  private static Logger logger;
  
  TaskHandlerContext() {
    this.CORE_SIZE = Runtime.getRuntime().availableProcessors();
    this.workerPool = new ArrayList<>();
    this.run = new AtomicBoolean(true);
  }
  
  static {
    logger = LoggerFactory.getLogger(TaskHandlerContext.class);
  }
  
  public void initialize() {
    int size = this.CORE_SIZE;
    if (size > 16)
      size = 16; 
    logger.warn("初始化线程：" + size);
    for (int i = 1; i <= size; i++) {
      TaskWorker worker = new TaskWorker(i);
      this.workerPool.add(worker);
      (new NamedThreadFactory("message-task-handler_" + i)).newThread(worker).start();
    } 
  }
  
  public void acceptTask(AbstractDistributeTask task) {
    if (task == null)
      throw new NullPointerException("task is null"); 
    int distributeKey = task.distributeKey() % this.workerPool.size();
    ((TaskWorker)this.workerPool.get(distributeKey)).addTask(task);
  }
  
  public void shutDown() {
    this.run.set(false);
    LoggerUtils.error("关闭业务场景线程结束", new Object[0]);
  }
  
  public String statistics() {
    Map<Integer, Integer> map = new HashMap<>();
    for (TaskWorker worker : this.workerPool)
      map.put(Integer.valueOf(worker.id), Integer.valueOf(worker.taskQueue.size())); 
    return JsonUtils.map2String(map);
  }
  
  private class TaskWorker implements Runnable {
    private int id;
    
    private BlockingQueue<AbstractDistributeTask> taskQueue = new LinkedBlockingQueue<>();
    
    TaskWorker(int index) {
      this.id = index;
    }
    
    public void addTask(AbstractDistributeTask task) {
      this.taskQueue.add(task);
    }
    
    public void run() {
      while (TaskHandlerContext.this.run.get()) {
        try {
          AbstractDistributeTask task = this.taskQueue.take();
          task.markStartMillis();
          task.action();
          task.markEndMillis();
          if (task instanceof TimerTask) {
            TimerTask timerTask = (TimerTask)task;
            if (timerTask.canRunAgain())
              addTask(task); 
          } 
        } catch (Exception e) {
          TaskHandlerContext.logger.error("task Worker" + this.id, e);
        } 
      } 
    }
  }
}
