package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.lang3.time.StopWatch;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Db4PetEquipService {
  Logger logger = LoggerFactory.getLogger(Db4PetEquipService.class);
  
  private static volatile Db4PetEquipService instance;
  
  @Autowired
  CrossServerConfig crossServerConfig;
  
  @Autowired
  private Dao dao;
  
  public static Db4PetEquipService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-pet_equip-save-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<PetEquip> queue = (BlockingQueue<PetEquip>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(PetEquip entity) {
    this.queue.add(entity);
  }
  
  public int size() {
    return this.queue.size();
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (Db4PetEquipService.this.run.get()) {
        PetEquip entity = null;
        try {
          entity = Db4PetEquipService.this.queue.take();
          StopWatch stopWatch = new StopWatch();
          stopWatch.start();
          Db4PetEquipService.this.saveToDb(entity);
          stopWatch.stop();
          if (stopWatch.getTime() > 500L) {
            LoggerUtils.info("宠物装备数据持久化消耗毫秒：" + stopWatch.getTime());
            LoggerUtils.info("宠物装备数据持久化队列长度：" + Db4PetEquipService.this.queue.size());
          } 
        } catch (Exception e) {
          Db4PetEquipService.this.logger.error("宠物装备存档异常", e);
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(PetEquip entity) {
    entity.doBeforeSave();
    if (entity.isDelete()) {
      this.dao.delete(entity);
    } else {
      this.dao.insertOrUpdate(entity);
    } 
  }
  
  @PreDestroy
  public void shutDown() {
    this.run.getAndSet(false);
    while (!this.queue.isEmpty())
      saveAllBeforeShutDown(); 
    LoggerUtils.error("[db4Equip] 执行全部命令后关闭");
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<PetEquip> it = this.queue.iterator();
      while (it.hasNext()) {
        PetEquip obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
