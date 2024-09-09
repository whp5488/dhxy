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
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Db4EquipService
{
    Logger logger;
    private static volatile Db4EquipService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<WearEquip> queue;
    private final AtomicBoolean run;
    
    public Db4EquipService() {
        this.logger = LoggerFactory.getLogger(Db4EquipService.class);
        this.queue = new BlockingUniqueQueue();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4EquipService getInstance() {
        return Db4EquipService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-equip-save-service").newThread(new Db4EquipService.Worker()).start();
        Db4EquipService.instance = this;
    }
    
    public void add2Queue(WearEquip entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(WearEquip entity) {
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
        LoggerUtils.error("[db4Equip] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            Iterator<WearEquip> it = this.queue.iterator();
            while (it.hasNext()) {
                WearEquip pet = (WearEquip)it.next();
                it.remove();
                this.saveToDb(pet);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4EquipService.this.run.get()) {
                WearEquip entity = null;
                try {
                    entity = (WearEquip)Db4EquipService.this.queue.take();
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    Db4EquipService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() > 500L) {
                        LoggerUtils.info("装备数据持久化消耗毫秒：" + stopWatch.getTime(), new Object[0]);
                        LoggerUtils.info("装备数据持久化队列长度：" + Db4EquipService.this.queue.size(), new Object[0]);
                    }
                    else {
                        continue;
                    }
                }
                catch (Exception e) {
                    Db4EquipService.this.logger.error("装备存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
