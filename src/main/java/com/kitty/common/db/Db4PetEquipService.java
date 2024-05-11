
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
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Db4PetEquipService
{
    Logger logger;
    private static volatile Db4PetEquipService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<PetEquip> queue;
    private final AtomicBoolean run;
    
    public Db4PetEquipService() {
        this.logger = LoggerFactory.getLogger(Db4PetEquipService.class);
        this.queue = new BlockingUniqueQueue<PetEquip>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4PetEquipService getInstance() {
        return Db4PetEquipService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-pet_equip-save-service").newThread(new Worker()).start();
        Db4PetEquipService.instance = this;
    }
    
    public void add2Queue(final PetEquip entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(final PetEquip entity) {
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
        LoggerUtils.error("[db4Equip] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<PetEquip> it = this.queue.iterator();
            while (it.hasNext()) {
                final PetEquip obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4PetEquipService.this.run.get()) {
                PetEquip entity = null;
                try {
                    entity = Db4PetEquipService.this.queue.take();
                    final StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    Db4PetEquipService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() <= 500L) {
                        continue;
                    }
                    LoggerUtils.info("宠物装备数据持久化消耗毫秒：" + stopWatch.getTime());
                    LoggerUtils.info("宠物装备数据持久化队列长度：" + Db4PetEquipService.this.queue.size());
                }
                catch (final Exception e) {
                    Db4PetEquipService.this.logger.error("宠物装备存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
