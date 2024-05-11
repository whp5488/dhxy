
package com.kitty.common.db;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.BlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Component;

@Component
public class Db4EmailService
{
    private static volatile Db4EmailService instance;
    @Autowired
    private Dao dao;
    private BlockingQueue<BaseEntity> queue;
    private final AtomicBoolean run;
    
    public Db4EmailService() {
        this.queue = new BlockingUniqueQueue<BaseEntity>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4EmailService getInstance() {
        return Db4EmailService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-email-save-service").newThread(new Worker()).start();
        Db4EmailService.instance = this;
    }
    
    public void add2Queue(final BaseEntity entity) {
        if (entity == null) {
            return;
        }
        this.queue.add(entity);
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
        LoggerUtils.error("[Db4Email] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<BaseEntity> it = (Iterator<BaseEntity>)this.queue.iterator();
            while (it.hasNext()) {
                final BaseEntity next = it.next();
                it.remove();
                this.saveToDb(next);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4EmailService.this.run.get()) {
                BaseEntity entity = null;
                try {
                    entity = Db4EmailService.this.queue.take();
                    Db4EmailService.this.saveToDb(entity);
                }
                catch (final Exception e) {
                    LoggerUtils.error("邮件数据持久化队列", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
