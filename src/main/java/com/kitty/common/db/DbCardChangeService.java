
package com.kitty.common.db;

import org.apache.commons.lang3.time.StopWatch;
import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.card.domain.CardChange;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.springframework.stereotype.Component;

@Component
public class DbCardChangeService
{
    private static volatile DbCardChangeService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<CardChange> queue;
    private final AtomicBoolean run;
    
    public DbCardChangeService() {
        this.queue = new BlockingUniqueQueue<CardChange>();
        this.run = new AtomicBoolean(true);
    }
    
    public static DbCardChangeService getInstance() {
        return DbCardChangeService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-card-change-save-service").newThread(new Worker()).start();
        DbCardChangeService.instance = this;
    }
    
    public void add2Queue(final CardChange entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(final CardChange entity) {
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
        LoggerUtils.error("[DbCardChange] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<CardChange> it = this.queue.iterator();
            while (it.hasNext()) {
                final CardChange obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (DbCardChangeService.this.run.get()) {
                CardChange entity = null;
                try {
                    entity = DbCardChangeService.this.queue.take();
                    final StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    DbCardChangeService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() <= 500L) {
                        continue;
                    }
                    LoggerUtils.info("变身卡-卡包数据持久化消耗毫秒：" + stopWatch.getTime());
                    LoggerUtils.info("变身卡-卡包数据持久化队列长度：" + DbCardChangeService.this.queue.size());
                }
                catch (final Exception e) {
                    LoggerUtils.error("变身卡存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
