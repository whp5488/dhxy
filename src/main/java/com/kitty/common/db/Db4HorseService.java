package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.horse.domain.Horse;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.lang3.time.StopWatch;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Db4HorseService {
  private static volatile Db4HorseService instance;
  
  @Autowired
  CrossServerConfig crossServerConfig;
  
  @Autowired
  private Dao dao;
  
  public static Db4HorseService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-horse-save-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<Horse> queue = (BlockingQueue<Horse>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(Horse entity) {
    this.queue.add(entity);
  }
  
  public int size() {
    return this.queue.size();
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (Db4HorseService.this.run.get()) {
        Horse entity = null;
        try {
          entity = Db4HorseService.this.queue.take();
          StopWatch stopWatch = new StopWatch();
          stopWatch.start();
          Db4HorseService.this.saveToDb(entity);
          stopWatch.stop();
          if (stopWatch.getTime() > 500L) {
            LoggerUtils.info("坐骑数据持久化消耗毫秒：" + stopWatch.getTime(), new Object[0]);
            LoggerUtils.info("坐骑数据持久化队列长度：" + Db4HorseService.this.queue.size(), new Object[0]);
          } 
        } catch (Exception e) {
          LoggerUtils.error("坐骑数据存档异常", e);
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(Horse entity) {
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
    LoggerUtils.error("[db4Horse] 执行全部命令后关闭", new Object[0]);
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<Horse> it = this.queue.iterator();
      while (it.hasNext()) {
        Horse obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
