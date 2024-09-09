package com.kitty.record;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.record.domain.FightRecordData;
import java.util.concurrent.BlockingQueue;
import com.kitty.common.start.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class FightRecordDataThread
{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ServerService serverService;
    private static volatile FightRecordDataThread instance;
    private BlockingQueue<FightRecordData> queue;
    private final AtomicBoolean run;
    
    public FightRecordDataThread() {
        this.queue = new BlockingUniqueQueue();
        this.run = new AtomicBoolean(true);
    }
    
    public static FightRecordDataThread getInstance() {
        return FightRecordDataThread.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("fight-record-data-service").newThread(new FightRecordDataThread.Worker()).start();
        FightRecordDataThread.instance = this;
    }
    
    public void add2Queue(FightRecordData fightRecordData) {
    }
    
    public void add2Queue(long id, long createTime, byte[] data) {
        FightRecordData fightRecordData = new FightRecordData(id, createTime, data);
        this.queue.add(fightRecordData);
    }
    
    private void saveToDb(FightRecordData fightRecordData) {
        this.mongoTemplate.insert(fightRecordData);
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
            Iterator<FightRecordData> it = this.queue.iterator();
            while (it.hasNext()) {
                FightRecordData obj = (FightRecordData)it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (FightRecordDataThread.this.run.get()) {
                try {
                    FightRecordData take = (FightRecordData)FightRecordDataThread.this.queue.take();
                    FightRecordDataThread.this.saveToDb(take);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
