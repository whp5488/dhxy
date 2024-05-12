
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
public class DbTianFuService
{
    private static volatile DbTianFuService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<BaseEntity> queue;
    private final AtomicBoolean run;
    
    public DbTianFuService() {
        this.queue = new BlockingUniqueQueue<BaseEntity>();
        this.run = new AtomicBoolean(true);
    }
    
    public static DbTianFuService getInstance() {
        return DbTianFuService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-tian-fu-save-service").newThread(new Worker()).start();
        DbTianFuService.instance = this;
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
        LoggerUtils.error("[DbStoreBag] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<BaseEntity> it = this.queue.iterator();
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
            while (DbTianFuService.this.run.get()) {
                BaseEntity entity = null;
                try {
                    entity = DbTianFuService.this.queue.take();
                    final StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    DbTianFuService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() <= 500L) {
                        continue;
                    }
                    LoggerUtils.info(entity.getClass().getName() + "数据持久化消耗毫秒：" + stopWatch.getTime());
                    LoggerUtils.info(entity.getClass().getName() + "数据持久化队列长度：" + DbTianFuService.this.queue.size());
                }
                catch (final Exception e) {
                    LoggerUtils.error(entity.getClass().getName() + "存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
