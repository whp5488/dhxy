
package com.kitty.common.core;

import com.kitty.logs.LoggerUtils;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import com.kitty.common.thread.NamedThreadFactory;
import java.util.concurrent.ScheduledExecutorService;

public class SchedulerManager
{
    private static SchedulerManager instance;
    private ScheduledExecutorService service;
    
    public static SchedulerManager getInstance() {
        if (SchedulerManager.instance != null) {
            return SchedulerManager.instance;
        }
        synchronized (SchedulerManager.class) {
            if (SchedulerManager.instance == null) {
                (SchedulerManager.instance = new SchedulerManager()).init();
            }
        }
        return SchedulerManager.instance;
    }
    
    @PostConstruct
    private void init() {
        this.service = Executors.newScheduledThreadPool(2, new NamedThreadFactory("common-scheduler"));
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period) {
        return this.service.scheduleAtFixedRate(new LogTask(command), initialDelay, period, TimeUnit.MILLISECONDS);
    }
    
    public ScheduledFuture<?> schedule(final Runnable command, final long delay) {
        return this.service.schedule(new LogTask(command), delay, TimeUnit.MILLISECONDS);
    }
    
    public void shutDown() {
        this.service.shutdown();
        this.service.shutdownNow();
        LoggerUtils.error("定时器关闭结束");
    }
    
    private static class LogTask implements Runnable
    {
        Runnable wrapper;
        
        public LogTask(final Runnable wrapper) {
            this.wrapper = wrapper;
        }
        
        @Override
        public void run() {
            try {
                this.wrapper.run();
            }
            catch (final Throwable e) {
                e.printStackTrace();
                LoggerUtils.error("定时任务执行异常", e.getMessage());
            }
        }
    }
}
