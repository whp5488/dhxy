package com.kitty.record;

import com.kitty.common.start.ServerService;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.logs.LoggerUtils;
import com.kitty.record.domain.FightRecord;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class FightRecordThread {
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private ServerService serverService;
  
  private static volatile FightRecordThread instance;
  
  public static FightRecordThread getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("fight-record-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<FightRecord> queue = (BlockingQueue<FightRecord>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(FightRecord fightRecord) {
    fightRecord.setMarking(this.serverService.getMarking());
    this.queue.add(fightRecord);
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (FightRecordThread.this.run.get()) {
        try {
          FightRecord take = FightRecordThread.this.queue.take();
          FightRecordThread.this.saveToDb(take);
        } catch (Exception e) {
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(FightRecord fightRecord) {
    this.mongoTemplate.insert(fightRecord);
  }
  
  @PreDestroy
  public void shutDown() {
    this.run.getAndSet(false);
    while (!this.queue.isEmpty())
      saveAllBeforeShutDown(); 
    LoggerUtils.error("[mongodb4FightRecord] 执行全部命令后关闭");
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<FightRecord> it = this.queue.iterator();
      while (it.hasNext()) {
        FightRecord obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
