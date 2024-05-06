package com.kitty.record;

import com.kitty.common.start.ServerService;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.logs.LoggerUtils;
import com.kitty.record.domain.Record;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class RecordThread {
  @Autowired
  private IdWorker idWorker;
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private ServerService serverService;
  
  private static volatile RecordThread instance;
  
  public static RecordThread getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("record-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<Record> queue = (BlockingQueue<Record>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(Record entity) {
    this.queue.add(entity);
  }
  
  public void add2Queue(long id, int roleId, short type, byte classify, String string, String marking) {
    Record record = new Record(id, roleId, type, classify, string, System.currentTimeMillis(), marking);
    this.queue.add(record);
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (RecordThread.this.run.get()) {
        try {
          Record take = RecordThread.this.queue.take();
          RecordThread.this.saveToDb(take);
        } catch (Exception e) {
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(Record entity) {
    this.mongoTemplate.insert(entity);
  }
  
  @PreDestroy
  public void shutDown() {
    this.run.getAndSet(false);
    while (!this.queue.isEmpty())
      saveAllBeforeShutDown(); 
    LoggerUtils.error("[db4Record] 执行全部命令后关闭");
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<Record> it = this.queue.iterator();
      while (it.hasNext()) {
        Record obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
