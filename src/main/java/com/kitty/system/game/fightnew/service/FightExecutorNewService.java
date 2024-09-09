package com.kitty.system.game.fightnew.service;

import com.kitty.logs.LoggerUtils;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.annotation.PreDestroy;
import com.kitty.system.game.fightnew.task.FightingTask;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class FightExecutorNewService
{
    public static final ConcurrentHashMap<Integer, Integer> FIGHT_ROOMS;
    public static final ConcurrentHashMap<Integer, Fight> FIGHTS;
    public static final ConcurrentHashMap<Integer, Integer> PETID_ROLEID;
    private final List<FightExecutorNewService.TaskWorker> workerPool;
    private volatile boolean run;
    
    public FightExecutorNewService() {
        this.workerPool = new ArrayList<>();
        this.run = true;
    }
    
    @PostConstruct
    private void init() {
        for (int size = 9, i = 1; i <= size; ++i) {
            FightExecutorNewService.TaskWorker worker = new FightExecutorNewService.TaskWorker(i);
            this.workerPool.add(worker);
            new NamedThreadFactory("game-fightnew_" + i).newThread(worker).start();
        }
    }
    
    public void addFightTask(Fight fight, FightingTask task) {
        int size = this.workerPool.size();
        int index = fight.getId() % size;
        ((FightExecutorNewService.TaskWorker)this.workerPool.get(index)).addTask(task);
    }
    
    @PreDestroy
    public void shutDown() {
        this.run = false;
    }
    
    static {
        FIGHT_ROOMS = new ConcurrentHashMap();
        FIGHTS = new ConcurrentHashMap();
        PETID_ROLEID = new ConcurrentHashMap();
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
            while (FightExecutorNewService.this.run) {
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
