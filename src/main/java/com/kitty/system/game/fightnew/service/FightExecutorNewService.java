package com.kitty.system.game.fightnew.service;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.task.FightingTask;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class FightExecutorNewService {
  public static final ConcurrentHashMap<Integer, Integer> FIGHT_ROOMS = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Fight> FIGHTS = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Integer> PETID_ROLEID = new ConcurrentHashMap<>();
  
  private final List<TaskWorker> workerPool = new ArrayList<>();
  
  private volatile boolean run = true;
  
  @PostConstruct
  private void init() {
    int size = 9;
    for (int i = 1; i <= size; i++) {
      TaskWorker worker = new TaskWorker(i);
      this.workerPool.add(worker);
      (new NamedThreadFactory("game-fightnew_" + i)).newThread(worker).start();
    } 
  }
  
  public void addFightTask(Fight fight, FightingTask task) {
    int size = this.workerPool.size();
    int index = fight.getId() % size;
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
      while (FightExecutorNewService.this.run) {
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
