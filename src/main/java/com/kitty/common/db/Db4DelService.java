package com.kitty.common.db;

import com.kitty.common.start.ServerService;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerUtils;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Db4DelService {
  @Autowired
  ServerService serverService;
  
  Logger logger = LoggerFactory.getLogger(getClass());
  
  private static volatile Db4DelService instance;
  
  @Autowired
  private Dao dao;
  
  public static Db4DelService getInstance() {
    return instance;
  }
  
  @PostConstruct
  public void init() {
    for (int i = 0; i < 32; i++)
      (new NamedThreadFactory("db-common-save-service-" + i)).newThread(new Worker()).start(); 
    this.logger.info("初始化线程32条");
    instance = this;
  }
  
  private BlockingQueue<BaseEntity> queue = (BlockingQueue<BaseEntity>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void delQueue(BaseEntity entity) {
    if (entity == null)
      return; 
    this.queue.add(entity);
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (Db4DelService.this.run.get()) {
        BaseEntity entity = null;
        try {
          entity = Db4DelService.this.queue.take();
          ((Db4DelService)SpringUtils.getBean(Db4DelService.class)).delToDb(entity);
        } catch (Exception e) {
          LoggerUtils.error("", e);
          e.printStackTrace();
        } 
        Db4DelService.this.logger.error("队列剩余数量=={}", Integer.valueOf(Db4DelService.this.queue.size()));
      } 
    }
  }
  
  public void delToDb(BaseEntity entity) {
    this.dao.delete(entity);
  }
}
