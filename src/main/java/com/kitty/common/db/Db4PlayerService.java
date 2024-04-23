package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.player.PlayerService;
import com.kitty.system.game.role.domain.Role;
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
public class Db4PlayerService {
  Logger logger = LoggerFactory.getLogger(Db4PlayerService.class);
  
  private static volatile Db4PlayerService instance;
  
  @Autowired
  CrossServerConfig crossServerConfig;
  
  @Autowired
  PlayerService playerService;
  
  @Autowired
  private Dao dao;
  
  public static Db4PlayerService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-player-save-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<Role> queue = (BlockingQueue<Role>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(Role entity) {
    this.queue.add(entity);
    this.playerService.saveToCache(entity);
  }
  
  public int size() {
    return this.queue.size();
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (Db4PlayerService.this.run.get()) {
        Role entity = null;
        try {
          entity = Db4PlayerService.this.queue.take();
          StopWatch stopWatch = new StopWatch();
          stopWatch.start();
          Db4PlayerService.this.saveToDb(entity);
          stopWatch.stop();
          if (stopWatch.getTime() > 500L) {
            LoggerUtils.info("玩家数据持久化消耗毫秒：" + stopWatch.getTime(), new Object[0]);
            LoggerUtils.info("玩家数据持久化队列长度：" + Db4PlayerService.this.queue.size(), new Object[0]);
          } 
        } catch (Exception e) {
          Db4PlayerService.this.logger.error("存档异常角色编号：{}，SID：{}", entity.getRoleId(), entity.getSid());
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(Role entity) {
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
    LoggerUtils.error("[db4Player] 执行全部命令后关闭", new Object[0]);
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<Role> it = this.queue.iterator();
      while (it.hasNext()) {
        Role player = it.next();
        it.remove();
        saveToDb(player);
      } 
    } 
  }
}
