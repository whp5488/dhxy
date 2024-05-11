
package com.kitty.common.db;

import org.apache.commons.lang3.time.StopWatch;
import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.pet.domain.Pet;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Db4PetService
{
    Logger logger;
    private static volatile Db4PetService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<Pet> queue;
    private final AtomicBoolean run;
    
    public Db4PetService() {
        this.logger = LoggerFactory.getLogger(Db4PetService.class);
        this.queue = new BlockingUniqueQueue<Pet>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4PetService getInstance() {
        return Db4PetService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-pet-save-service").newThread(new Worker()).start();
        Db4PetService.instance = this;
    }
    
    public void add2Queue(final Pet entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(final Pet entity) {
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
        LoggerUtils.error("[db4Pet] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<Pet> it = this.queue.iterator();
            while (it.hasNext()) {
                final Pet pet = it.next();
                it.remove();
                this.saveToDb(pet);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4PetService.this.run.get()) {
                try {
                    Pet entity = Db4PetService.this.queue.take();
                    final StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    Db4PetService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() <= 500L) {
                        continue;
                    }
                    LoggerUtils.info("宠物数据持久化消耗毫秒：" + stopWatch.getTime());
                    LoggerUtils.info("宠物数据持久化队列长度：" + Db4PetService.this.queue.size());
                }
                catch (final Exception e) {
                    Db4PetService.this.logger.error("宠物存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
