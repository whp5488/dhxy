
package com.kitty.common.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory
{
    private ThreadGroup threadGroup;
    private String groupName;
    private final boolean daemo;
    private AtomicInteger idGenerator;
    
    public NamedThreadFactory(final String group) {
        this(group, false);
    }
    
    public NamedThreadFactory(final String group, final boolean daemo) {
        this.idGenerator = new AtomicInteger(1);
        this.groupName = group;
        this.daemo = daemo;
    }
    
    @Override
    public Thread newThread(final Runnable r) {
        final String name = this.getNextThreadName();
        final Thread ret = new Thread(this.threadGroup, r, name, 0L);
        ret.setDaemon(this.daemo);
        return ret;
    }
    
    private String getNextThreadName() {
        return this.groupName + "-thread-" + this.idGenerator.getAndIncrement();
    }
}
