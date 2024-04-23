package com.kitty.record;

import com.kitty.common.start.ServerService;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.logs.LoggerUtils;
import com.kitty.record.domain.FightRecordData;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class FightRecordDataThread {
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private ServerService serverService;
  
  private static volatile FightRecordDataThread instance;
  
  public static FightRecordDataThread getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("fight-record-data-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<FightRecordData> queue = (BlockingQueue<FightRecordData>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(FightRecordData fightRecordData) {
    this.queue.add(fightRecordData);
  }
  
  public void add2Queue(long id, long createTime, byte[] data) {
    FightRecordData fightRecordData = new FightRecordData(id, createTime, data);
    this.queue.add(fightRecordData);
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (FightRecordDataThread.this.run.get()) {
        try {
          FightRecordData take = FightRecordDataThread.this.queue.take();
          FightRecordDataThread.this.saveToDb(take);
        } catch (Exception e) {
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(FightRecordData fightRecordData) {
    this.mongoTemplate.insert(fightRecordData);
  }
  
  @PreDestroy
  public void shutDown() {
    this.run.getAndSet(false);
    while (!this.queue.isEmpty())
      saveAllBeforeShutDown(); 
    LoggerUtils.error("[mongodb4FightRecord] 执行全部命令后关闭", new Object[0]);
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<FightRecordData> it = this.queue.iterator();
      while (it.hasNext()) {
        FightRecordData obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
