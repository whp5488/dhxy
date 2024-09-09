package com.kitty.system.game.fightnew.spectator.service;

import com.kitty.logs.LoggerUtils;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.annotation.PreDestroy;
import com.kitty.system.game.fightnew.task.FightingTask;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FightSpectatorExecutorNewService
{
    private final List<FightSpectatorExecutorNewService.TaskWorker> workerPool;
    private volatile boolean run;
    
    public FightSpectatorExecutorNewService() {
        this.workerPool = new ArrayList<>();
        this.run = true;
    }
    
    @PostConstruct
    private void init() {
        for (int size = 2, i = 1; i <= size; ++i) {
            FightSpectatorExecutorNewService.TaskWorker worker = new FightSpectatorExecutorNewService.TaskWorker(i);
            this.workerPool.add(worker);
            new NamedThreadFactory("game-fightnew-spectator_" + i).newThread(worker).start();
        }
    }
    
    public void addFightTask(int fightId, FightingTask task) {
        int size = this.workerPool.size();
        int index = fightId % size;
        ((FightSpectatorExecutorNewService.TaskWorker)this.workerPool.get(index)).addTask(task);
    }
    
    @PreDestroy
    public void shutDown() {
        this.run = false;
    }
    
    private class TaskWorker implements Runnable
    {
        private int id;
        private BlockingQueue<FightingTask> taskQueue;
        
        TaskWorker(int index) {
            this.taskQueue = new LinkedBlockingQueue();
            this.id = index;
        }
        
        public void addTask(FightingTask task) {
            this.taskQueue.add(task);
        }
        
        @Override
        public void run() {
            while (FightSpectatorExecutorNewService.this.run) {
                try {
                    FightingTask task = (FightingTask)this.taskQueue.take();
                    task.run();
                }
                catch (Exception e) {
                    LoggerUtils.error("task Worker" + this.id, e);
                }
            }
        }
    }
}
