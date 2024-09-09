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
public class Db4EventService
{
    private static volatile Db4EventService instance;
    @Autowired
    private Dao dao;
    private BlockingQueue<BaseEntity> queue;
    private final AtomicBoolean run;
    
    public Db4EventService() {
        this.queue = new BlockingUniqueQueue<>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4EventService getInstance() {
        return Db4EventService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-event-save-service").newThread(new Db4EventService.Worker()).start();
        Db4EventService.instance = this;
    }
    
    public void add2Queue(BaseEntity entity) {
        if (entity == null) {
            return;
        }
        this.queue.add(entity);
    }
    
    private void saveToDb(BaseEntity entity) {
        entity.doBeforeSave();
        if (entity.isDelete()) {
            this.dao.delete(entity);
        }
        else {
            this.dao.insertOrUpdate(entity);
        }
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[Db4Event] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            Iterator<BaseEntity> it = this.queue.iterator();
            while (it.hasNext()) {
                BaseEntity next = it.next();
                it.remove();
                this.saveToDb(next);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4EventService.this.run.get()) {
                BaseEntity entity = null;
                try {
                    entity = Db4EventService.this.queue.take();
                    Db4EventService.this.saveToDb(entity);
                }
                catch (Exception e) {
                    LoggerUtils.error("区组活动公共数据持久化队列异常");
                    e.printStackTrace();
                }
            }
        }
    }
}
