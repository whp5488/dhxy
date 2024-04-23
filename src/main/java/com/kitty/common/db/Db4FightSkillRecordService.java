package com.kitty.common.db;

import com.kitty.common.start.ServerService;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.fightRecord.domain.FightSkillRecord;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class Db4FightSkillRecordService {
  private static volatile Db4FightSkillRecordService instance;
  
  @Autowired
  private Dao dao;
  
  @Autowired
  private ServerService serverService;
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  public static Db4FightSkillRecordService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-fight-skill-record-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<FightSkillRecord> queue = (BlockingQueue<FightSkillRecord>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(FightSkillRecord record) {
    if (record == null)
      return; 
    record.setMarking(this.serverService.getMarking());
    this.queue.add(record);
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (Db4FightSkillRecordService.this.run.get()) {
        FightSkillRecord record = null;
        try {
          record = Db4FightSkillRecordService.this.queue.take();
          Db4FightSkillRecordService.this.saveToDb(record);
        } catch (Exception e) {
          LoggerUtils.error("公共数据(排查玩家数据)持久化队列", e);
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(FightSkillRecord record) {
    record.doBeforeSave();
    this.mongoTemplate.insert(record);
  }
  
  @PreDestroy
  public void shutDown() {
    this.run.getAndSet(false);
    while (!this.queue.isEmpty())
      saveAllBeforeShutDown(); 
    LoggerUtils.error("[Db4FightSkillRecord] 执行全部命令后关闭", new Object[0]);
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<FightSkillRecord> it = this.queue.iterator();
      while (it.hasNext()) {
        FightSkillRecord next = it.next();
        it.remove();
        saveToDb(next);
      } 
    } 
  }
}
