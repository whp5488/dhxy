
package com.kitty.record;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.record.domain.Record;
import java.util.concurrent.BlockingQueue;
import com.kitty.common.start.ServerService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.thread.IdWorker;
import org.springframework.stereotype.Component;

@Component
public class RecordThread
{
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ServerService serverService;
    private static volatile RecordThread instance;
    private BlockingQueue<Record> queue;
    private final AtomicBoolean run;
    
    public RecordThread() {
        this.queue = new BlockingUniqueQueue<Record>();
        this.run = new AtomicBoolean(true);
    }
    
    public static RecordThread getInstance() {
        return RecordThread.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("record-service").newThread(new Worker()).start();
        RecordThread.instance = this;
    }
    
    public void add2Queue(final Record entity) {
        this.queue.add(entity);
    }
    
    public void add2Queue(final long id, final int roleId, final short type, final byte classify, final String string, final String marking) {
        final Record record = new Record(id, roleId, type, classify, string, System.currentTimeMillis(), marking);
        this.queue.add(record);
    }
    
    private void saveToDb(final Record entity) {
        this.mongoTemplate.insert((Object)entity);
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[db4Record] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<Record> it = this.queue.iterator();
            while (it.hasNext()) {
                final Record obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (RecordThread.this.run.get()) {
                try {
                    final Record take = RecordThread.this.queue.take();
                    RecordThread.this.saveToDb(take);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
