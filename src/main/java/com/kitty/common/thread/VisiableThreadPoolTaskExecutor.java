
package com.kitty.common.thread;

import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor
{
    private static final Logger logger;
    
    private void showThreadPoolInfo(final String prefix) {
        final ThreadPoolExecutor threadPoolExecutor = this.getThreadPoolExecutor();
        if (null == threadPoolExecutor) {
            return;
        }
        VisiableThreadPoolTaskExecutor.logger.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]", this.getThreadNamePrefix(), prefix, threadPoolExecutor.getTaskCount(), threadPoolExecutor.getCompletedTaskCount(), threadPoolExecutor.getActiveCount(), threadPoolExecutor.getQueue().size());
    }
    
    public void execute(final Runnable task) {
        this.showThreadPoolInfo("1. do execute");
        super.execute(task);
    }
    
    public void execute(final Runnable task, final long startTimeout) {
        this.showThreadPoolInfo("2. do execute");
        super.execute(task, startTimeout);
    }
    
    public Future<?> submit(final Runnable task) {
        this.showThreadPoolInfo("1. do submit");
        return super.submit(task);
    }
    
    public <T> Future<T> submit(final Callable<T> task) {
        this.showThreadPoolInfo("2. do submit");
        return super.submit(task);
    }
    
    public ListenableFuture<?> submitListenable(final Runnable task) {
        this.showThreadPoolInfo("1. do submitListenable");
        return super.submitListenable(task);
    }
    
    public <T> ListenableFuture<T> submitListenable(final Callable<T> task) {
        this.showThreadPoolInfo("2. do submitListenable");
        return super.submitListenable(task);
    }
    
    static {
        logger = LoggerFactory.getLogger(VisiableThreadPoolTaskExecutor.class);
    }
}
