package com.kitty.system.game.fightnew.spectator.service;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.fightnew.task.FightingTask;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class FightSpectatorExecutorNewService {
  private final List<TaskWorker> workerPool = new ArrayList<>();
  
  private volatile boolean run = true;
  
  @PostConstruct
  private void init() {
    int size = 2;
    for (int i = 1; i <= size; i++) {
      TaskWorker worker = new TaskWorker(i);
      this.workerPool.add(worker);
      (new NamedThreadFactory("game-fightnew-spectator_" + i)).newThread(worker).start();
    } 
  }
  
  public void addFightTask(int fightId, FightingTask task) {
    int size = this.workerPool.size();
    int index = fightId % size;
    ((TaskWorker)this.workerPool.get(index)).addTask(task);
  }
  
  @PreDestroy
  public void shutDown() {
    this.run = false;
  }
  
  private class TaskWorker implements Runnable {
    private int id;
    
    private BlockingQueue<FightingTask> taskQueue = new LinkedBlockingQueue<>();
    
    TaskWorker(int index) {
      this.id = index;
    }
    
    public void addTask(FightingTask task) {
      this.taskQueue.add(task);
    }
    
    public void run() {
      while (FightSpectatorExecutorNewService.this.run) {
        try {
          FightingTask task = this.taskQueue.take();
          task.run();
        } catch (Exception e) {
          LoggerUtils.error("task Worker" + this.id, e);
        } 
      } 
    }
  }
}
