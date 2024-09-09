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
public class Db4CommonService
{
    private static volatile Db4CommonService instance;
    @Autowired
    private Dao dao;
    private BlockingQueue<BaseEntity> queue;
    private final AtomicBoolean run;
    
    public Db4CommonService() {
        this.queue = new BlockingUniqueQueue();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4CommonService getInstance() {
        return Db4CommonService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-common-save-service").newThread(new Db4CommonService.Worker()).start();
        Db4CommonService.instance = this;
    }
    
    public void add2Queue(BaseEntity entity) {
        if (entity == null) {
            return;
        }
        this.queue.add(entity);
        return;
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
        LoggerUtils.error("[Db4Common] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            Iterator<BaseEntity> it = this.queue.iterator();
            while (it.hasNext()) {
                BaseEntity next = (BaseEntity)it.next();
                it.remove();
                this.saveToDb(next);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4CommonService.this.run.get()) {
                BaseEntity entity = null;
                try {
                    entity = (BaseEntity)Db4CommonService.this.queue.take();
                    Db4CommonService.this.saveToDb(entity);
                }
                catch (Exception e) {
                    LoggerUtils.error("公共数据(排查玩家数据)持久化队列", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
