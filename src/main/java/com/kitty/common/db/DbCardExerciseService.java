
package com.kitty.common.db;

import org.apache.commons.lang3.time.StopWatch;
import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.card.domain.CardExercise;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.springframework.stereotype.Component;

@Component
public class DbCardExerciseService
{
    private static volatile DbCardExerciseService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<CardExercise> queue;
    private final AtomicBoolean run;
    
    public DbCardExerciseService() {
        this.queue = new BlockingUniqueQueue<CardExercise>();
        this.run = new AtomicBoolean(true);
    }
    
    public static DbCardExerciseService getInstance() {
        return DbCardExerciseService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-card-exercise-save-service").newThread(new Worker()).start();
        DbCardExerciseService.instance = this;
    }
    
    public void add2Queue(final CardExercise entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(final CardExercise entity) {
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
        LoggerUtils.error("[DbCardExercise] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<CardExercise> it = this.queue.iterator();
            while (it.hasNext()) {
                final CardExercise obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (DbCardExerciseService.this.run.get()) {
                CardExercise entity = null;
                try {
                    entity = DbCardExerciseService.this.queue.take();
                    final StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    DbCardExerciseService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() <= 500L) {
                        continue;
                    }
                    LoggerUtils.info("变身卡五行修炼数据持久化消耗毫秒：" + stopWatch.getTime());
                    LoggerUtils.info("变身卡五行修炼据持久化队列长度：" + DbCardExerciseService.this.queue.size());
                }
                catch (final Exception e) {
                    LoggerUtils.error("存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
