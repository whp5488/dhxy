package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.logs.LoggerUtils;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Db4EmailService {
  private static volatile Db4EmailService instance;
  
  @Autowired
  private Dao dao;
  
  public static Db4EmailService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-email-save-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<BaseEntity> queue = (BlockingQueue<BaseEntity>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(BaseEntity entity) {
    if (entity == null)
      return; 
    this.queue.add(entity);
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (Db4EmailService.this.run.get()) {
        BaseEntity entity = null;
        try {
          entity = Db4EmailService.this.queue.take();
          Db4EmailService.this.saveToDb(entity);
        } catch (Exception e) {
          LoggerUtils.error("邮件数据持久化队列", e);
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(BaseEntity entity) {
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
    LoggerUtils.error("[Db4Email] 执行全部命令后关闭", new Object[0]);
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<BaseEntity> it = this.queue.iterator();
      while (it.hasNext()) {
        BaseEntity next = it.next();
        it.remove();
        saveToDb(next);
      } 
    } 
  }
}
