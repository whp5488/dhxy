package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
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
public class DbBagOrStoreService {
  private static volatile DbBagOrStoreService instance;
  
  @Autowired
  CrossServerConfig crossServerConfig;
  
  @Autowired
  private Dao dao;
  
  public static DbBagOrStoreService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-bag-store-save-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<BaseEntity> queue = (BlockingQueue<BaseEntity>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(BaseEntity entity) {
    this.queue.add(entity);
  }
  
  public int size() {
    return this.queue.size();
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (DbBagOrStoreService.this.run.get()) {
        BaseEntity entity = null;
        try {
          entity = DbBagOrStoreService.this.queue.take();
          StopWatch stopWatch = new StopWatch();
          stopWatch.start();
          DbBagOrStoreService.this.saveToDb(entity);
          stopWatch.stop();
          if (stopWatch.getTime() > 500L) {
            LoggerUtils.info(entity.getClass().getName() + "数据持久化消耗毫秒：" + stopWatch.getTime());
            LoggerUtils.info(entity.getClass().getName() + "数据持久化队列长度：" + DbBagOrStoreService.this.queue.size(), new Object[0]);
          } 
        } catch (Exception e) {
          LoggerUtils.error(entity.getClass().getName() + "存档异常", e);
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
    LoggerUtils.error("[DbStoreBag] 执行全部命令后关闭");
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<BaseEntity> it = this.queue.iterator();
      while (it.hasNext()) {
        BaseEntity obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
