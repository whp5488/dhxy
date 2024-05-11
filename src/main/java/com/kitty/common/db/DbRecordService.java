
package com.kitty.common.db;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.springframework.stereotype.Component;

@Component
public class DbRecordService
{
    private static volatile DbRecordService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<BaseEntity> queue;
    private final AtomicBoolean run;
    
    public DbRecordService() {
        this.queue = new BlockingUniqueQueue<BaseEntity>();
        this.run = new AtomicBoolean(true);
    }
    
    public static DbRecordService getInstance() {
        return DbRecordService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-record-save-service").newThread(new Worker()).start();
        DbRecordService.instance = this;
    }
    
    public void add2Queue(final BaseEntity entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(final BaseEntity entity) {
        entity.doBeforeSave();
        if (entity.isDelete()) {
            this.dao.delete((Object)entity);
        }
        else {
            this.dao.insertOrUpdate((Object)entity);
        }
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[DbRecord] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<BaseEntity> it = (Iterator<BaseEntity>)this.queue.iterator();
            while (it.hasNext()) {
                final BaseEntity obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (DbRecordService.this.run.get()) {
                BaseEntity entity = null;
                try {
                    entity = DbRecordService.this.queue.take();
                    DbRecordService.this.saveToDb(entity);
                }
                catch (final Exception e) {
                    LoggerUtils.error("存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
