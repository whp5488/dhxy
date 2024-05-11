
package com.kitty.cross.core;

import com.kitty.cross.core.client.CCSession;
import com.kitty.cross.core.server.SCSession;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import com.kitty.common.thread.NamedThreadFactory;
import java.util.concurrent.ExecutorService;

public class CrossCmdExecutor
{
    private static volatile CrossCmdExecutor self;
    private final int DEFAULT_CORE_SUM;
    private ExecutorService[] services;
    
    public CrossCmdExecutor() {
        this.DEFAULT_CORE_SUM = Runtime.getRuntime().availableProcessors() - 2;
    }
    
    public static CrossCmdExecutor getInstance() {
        if (CrossCmdExecutor.self != null) {
            return CrossCmdExecutor.self;
        }
        synchronized (CrossCmdExecutor.class) {
            if (CrossCmdExecutor.self == null) {
                final CrossCmdExecutor instance = new CrossCmdExecutor();
                instance.init();
                CrossCmdExecutor.self = instance;
            }
        }
        return CrossCmdExecutor.self;
    }
    
    private void init() {
        this.services = new ExecutorService[this.DEFAULT_CORE_SUM];
        for (int i = 0; i < this.DEFAULT_CORE_SUM; ++i) {
            this.services[i] = Executors.newSingleThreadExecutor(new NamedThreadFactory("cross-service-" + i));
        }
    }
    
    public void addTask(final SCSession session, final Runnable task) {
        final int sessionId = session.getId();
        final int index = sessionId % this.services.length;
        this.services[index].submit(task);
    }
    
    public void addTask(final CCSession session, final Runnable task) {
        final int sessionId = session.getId();
        final int index = sessionId % this.services.length;
        this.services[index].submit(task);
    }
    
    public void shutDown() {
        for (int i = 0; i < this.DEFAULT_CORE_SUM; ++i) {
            this.services[i].shutdown();
        }
    }
}
