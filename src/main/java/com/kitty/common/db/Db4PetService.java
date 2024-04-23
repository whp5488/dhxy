package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.pet.domain.Pet;
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
public class Db4PetService {
  Logger logger = LoggerFactory.getLogger(Db4PetService.class);
  
  private static volatile Db4PetService instance;
  
  @Autowired
  CrossServerConfig crossServerConfig;
  
  @Autowired
  private Dao dao;
  
  public static Db4PetService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-pet-save-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<Pet> queue = (BlockingQueue<Pet>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(Pet entity) {
    this.queue.add(entity);
  }
  
  public int size() {
    return this.queue.size();
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (Db4PetService.this.run.get()) {
        Pet entity = null;
        try {
          entity = Db4PetService.this.queue.take();
          StopWatch stopWatch = new StopWatch();
          stopWatch.start();
          Db4PetService.this.saveToDb(entity);
          stopWatch.stop();
          if (stopWatch.getTime() > 500L) {
            LoggerUtils.info("宠物数据持久化消耗毫秒：" + stopWatch.getTime(), new Object[0]);
            LoggerUtils.info("宠物数据持久化队列长度：" + Db4PetService.this.queue.size(), new Object[0]);
          } 
        } catch (Exception e) {
          Db4PetService.this.logger.error("宠物存档异常", e);
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(Pet entity) {
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
    LoggerUtils.error("[db4Pet] 执行全部命令后关闭", new Object[0]);
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<Pet> it = this.queue.iterator();
      while (it.hasNext()) {
        Pet pet = it.next();
        it.remove();
        saveToDb(pet);
      } 
    } 
  }
}
