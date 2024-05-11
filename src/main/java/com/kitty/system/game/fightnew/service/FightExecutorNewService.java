
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
    private final List<TaskWorker> workerPool;
    private volatile boolean run;
    
    public FightExecutorNewService() {
        this.workerPool = new ArrayList<TaskWorker>();
        this.run = true;
    }
    
    @PostConstruct
    private void init() {
        for (int size = 9, i = 1; i <= size; ++i) {
            final TaskWorker worker = new TaskWorker(i);
            this.workerPool.add(worker);
            new NamedThreadFactory("game-fightnew_" + i).newThread(worker).start();
        }
    }
    
    public void addFightTask(final Fight fight, final FightingTask task) {
        final int size = this.workerPool.size();
        final int index = fight.getId() % size;
        this.workerPool.get(index).addTask(task);
    }
    
    @PreDestroy
    public void shutDown() {
        this.run = false;
    }
    
    static {
        FIGHT_ROOMS = new ConcurrentHashMap<Integer, Integer>();
        FIGHTS = new ConcurrentHashMap<Integer, Fight>();
        PETID_ROLEID = new ConcurrentHashMap<Integer, Integer>();
    }
    
    private class TaskWorker implements Runnable
    {
        private int id;
        private BlockingQueue<FightingTask> taskQueue;
        
        TaskWorker(final int index) {
            this.taskQueue = new LinkedBlockingQueue<FightingTask>();
            this.id = index;
        }
        
        public void addTask(final FightingTask task) {
            this.taskQueue.add(task);
        }
        
        @Override
        public void run() {
            while (FightExecutorNewService.this.run) {
                try {
                    final FightingTask task = this.taskQueue.take();
                    task.run();
                }
                catch (final Exception e) {
                    LoggerUtils.error("task Worker" + this.id, e);
                }
            }
        }
    }
}
