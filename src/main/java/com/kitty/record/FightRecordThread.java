
package com.kitty.record;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.record.domain.FightRecord;
import java.util.concurrent.BlockingQueue;
import com.kitty.common.start.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class FightRecordThread
{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ServerService serverService;
    private static volatile FightRecordThread instance;
    private BlockingQueue<FightRecord> queue;
    private final AtomicBoolean run;
    
    public FightRecordThread() {
        this.queue = new BlockingUniqueQueue<FightRecord>();
        this.run = new AtomicBoolean(true);
    }
    
    public static FightRecordThread getInstance() {
        return FightRecordThread.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("fight-record-service").newThread(new Worker()).start();
        FightRecordThread.instance = this;
    }
    
    public void add2Queue(final FightRecord fightRecord) {
        fightRecord.setMarking(this.serverService.getMarking());
        this.queue.add(fightRecord);
    }
    
    private void saveToDb(final FightRecord fightRecord) {
        this.mongoTemplate.insert((Object)fightRecord);
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[mongodb4FightRecord] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<FightRecord> it = this.queue.iterator();
            while (it.hasNext()) {
                final FightRecord obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (FightRecordThread.this.run.get()) {
                try {
                    final FightRecord take = FightRecordThread.this.queue.take();
                    FightRecordThread.this.saveToDb(take);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
