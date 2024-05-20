
package com.kitty.mina.task;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.Map;
import com.kitty.common.utils.JsonUtils;
import java.util.HashMap;
import com.kitty.logs.LoggerUtils;
import com.kitty.common.thread.NamedThreadFactory;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import org.slf4j.Logger;

public enum TaskHandlerContext
{
    INSTANCE;
    
    private static Logger logger;
    private final int CORE_SIZE;
    private final List<TaskWorker> workerPool;
    private final AtomicBoolean run;
    
    private TaskHandlerContext() {
        this.CORE_SIZE = Runtime.getRuntime().availableProcessors();
        this.workerPool = new ArrayList<>();
        this.run = new AtomicBoolean(true);
    }
    
    public void initialize() {
        int size = this.CORE_SIZE;
        if (size > 16) {
            size = 16;
        }
        TaskHandlerContext.logger.warn("初始化线程：" + size);
        for (int i = 1; i <= size; ++i) {
            final TaskWorker worker = new TaskWorker(i);
            this.workerPool.add(worker);
            new NamedThreadFactory("message-task-handler_" + i).newThread(worker).start();
        }
    }
    
    public void acceptTask(final AbstractDistributeTask task) {
        if (task == null) {
            throw new NullPointerException("task is null");
        }
        final int distributeKey = task.distributeKey() % this.workerPool.size();
        this.workerPool.get(distributeKey).addTask(task);
    }
    
    public void shutDown() {
        this.run.set(false);
        LoggerUtils.error("关闭业务场景线程结束");
    }
    
    public String statistics() {
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (final TaskWorker worker : this.workerPool) {
            map.put(worker.id, worker.taskQueue.size());
        }
        return JsonUtils.map2String(map);
    }
    
    static {
        TaskHandlerContext.logger = LoggerFactory.getLogger(TaskHandlerContext.class);
    }
    
    private class TaskWorker implements Runnable
    {
        private int id;
        private BlockingQueue<AbstractDistributeTask> taskQueue;
        
        TaskWorker(final int index) {
            this.taskQueue = new LinkedBlockingQueue<>();
            this.id = index;
        }
        
        public void addTask(final AbstractDistributeTask task) {
            this.taskQueue.add(task);
        }
        
        @Override
        public void run() {
            while (TaskHandlerContext.this.run.get()) {
                try {
                    final AbstractDistributeTask task = this.taskQueue.take();
                    task.markStartMillis();
                    task.action();
                    task.markEndMillis();
                    if (!(task instanceof TimerTask)) {
                        continue;
                    }
                    final TimerTask timerTask = (TimerTask)task;
                    if (!timerTask.canRunAgain()) {
                        continue;
                    }
                    this.addTask(task);
                }
                catch (final Exception e) {
                    TaskHandlerContext.logger.error("task Worker" + this.id, e);
                }
            }
        }
    }
}
