
package com.kitty.common.db;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.fightRecord.domain.FightRecord;
import java.util.concurrent.BlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Component;

@Component
public class Db4FightRecordService
{
    private static volatile Db4FightRecordService instance;
    @Autowired
    private Dao dao;
    private BlockingQueue<FightRecord> queue;
    private final AtomicBoolean run;
    
    public Db4FightRecordService() {
        this.queue = new BlockingUniqueQueue<FightRecord>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4FightRecordService getInstance() {
        return Db4FightRecordService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-fight-record-service").newThread(new Worker()).start();
        Db4FightRecordService.instance = this;
    }
    
    public void add2Queue(final FightRecord record) {
        if (record == null) {
            return;
        }
        this.queue.add(record);
    }
    
    private void saveToDb(final FightRecord record) {
        record.doBeforeSave();
        if (record.isDelete()) {
            this.dao.delete((Object)record);
        }
        else {
            this.dao.insertOrUpdate((Object)record);
        }
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[Db4FightRecord] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<FightRecord> it = this.queue.iterator();
            while (it.hasNext()) {
                final FightRecord next = it.next();
                it.remove();
                this.saveToDb(next);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4FightRecordService.this.run.get()) {
                FightRecord record = null;
                try {
                    record = Db4FightRecordService.this.queue.take();
                    Db4FightRecordService.this.saveToDb(record);
                }
                catch (final Exception e) {
                    LoggerUtils.error("公共数据(排查玩家数据)持久化队列", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
