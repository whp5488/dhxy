package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.card.domain.CardExercise;
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
public class DbCardExerciseService {
  private static volatile DbCardExerciseService instance;
  
  @Autowired
  CrossServerConfig crossServerConfig;
  
  @Autowired
  private Dao dao;
  
  public static DbCardExerciseService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-card-exercise-save-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<CardExercise> queue = (BlockingQueue<CardExercise>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(CardExercise entity) {
    this.queue.add(entity);
  }
  
  public int size() {
    return this.queue.size();
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (DbCardExerciseService.this.run.get()) {
        CardExercise entity = null;
        try {
          entity = DbCardExerciseService.this.queue.take();
          StopWatch stopWatch = new StopWatch();
          stopWatch.start();
          DbCardExerciseService.this.saveToDb(entity);
          stopWatch.stop();
          if (stopWatch.getTime() > 500L) {
            LoggerUtils.info("变身卡五行修炼数据持久化消耗毫秒：" + stopWatch.getTime());
            LoggerUtils.info("变身卡五行修炼据持久化队列长度：" + DbCardExerciseService.this.queue.size());
          } 
        } catch (Exception e) {
          LoggerUtils.error("存档异常", e);
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(CardExercise entity) {
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
    LoggerUtils.error("[DbCardExercise] 执行全部命令后关闭");
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<CardExercise> it = this.queue.iterator();
      while (it.hasNext()) {
        CardExercise obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
