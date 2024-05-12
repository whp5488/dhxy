
package com.kitty.common.db;

import org.apache.commons.lang3.time.StopWatch;
import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.horse.domain.Horse;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.springframework.stereotype.Component;

@Component
public class Db4HorseService
{
    private static volatile Db4HorseService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<Horse> queue;
    private final AtomicBoolean run;
    
    public Db4HorseService() {
        this.queue = new BlockingUniqueQueue<Horse>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4HorseService getInstance() {
        return Db4HorseService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-horse-save-service").newThread(new Worker()).start();
        Db4HorseService.instance = this;
    }
    
    public void add2Queue(final Horse entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(final Horse entity) {
        entity.doBeforeSave();
        if (entity.isDelete()) {
            this.dao.delete(entity);
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
        LoggerUtils.error("[db4Horse] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<Horse> it = this.queue.iterator();
            while (it.hasNext()) {
                final Horse obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4HorseService.this.run.get()) {
                Horse entity = null;
                try {
                    entity = Db4HorseService.this.queue.take();
                    final StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    Db4HorseService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() <= 500L) {
                        continue;
                    }
                    LoggerUtils.info("坐骑数据持久化消耗毫秒：" + stopWatch.getTime());
                    LoggerUtils.info("坐骑数据持久化队列长度：" + Db4HorseService.this.queue.size());
                }
                catch (final Exception e) {
                    LoggerUtils.error("坐骑数据存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
