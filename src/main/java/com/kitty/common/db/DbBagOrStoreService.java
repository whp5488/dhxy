package com.kitty.common.db;

import org.apache.commons.lang3.time.StopWatch;
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
public class DbBagOrStoreService
{
    private static volatile DbBagOrStoreService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<BaseEntity> queue;
    private final AtomicBoolean run;
    
    public DbBagOrStoreService() {
        this.queue = new BlockingUniqueQueue();
        this.run = new AtomicBoolean(true);
    }
    
    public static DbBagOrStoreService getInstance() {
        return DbBagOrStoreService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-bag-store-save-service").newThread(new DbBagOrStoreService.Worker()).start();
        DbBagOrStoreService.instance = this;
    }
    
    public void add2Queue(BaseEntity entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
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
        LoggerUtils.error("[DbStoreBag] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            Iterator<BaseEntity> it = this.queue.iterator();
            while (it.hasNext()) {
                BaseEntity obj = (BaseEntity)it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (DbBagOrStoreService.this.run.get()) {
                BaseEntity entity = null;
                try {
                    entity = (BaseEntity)DbBagOrStoreService.this.queue.take();
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    DbBagOrStoreService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() > 500L) {
                        LoggerUtils.info(entity.getClass().getName() + "数据持久化消耗毫秒：" + stopWatch.getTime(), new Object[0]);
                        LoggerUtils.info(entity.getClass().getName() + "数据持久化队列长度：" + DbBagOrStoreService.this.queue.size(), new Object[0]);
                    }
                    else {
                        continue;
                    }
                }
                catch (Exception e) {
                    LoggerUtils.error(entity.getClass().getName() + "存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
