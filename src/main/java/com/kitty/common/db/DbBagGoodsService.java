package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.cross.CrossServerConfig;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.bag.domain.BagGoods;
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
public class DbBagGoodsService {
  Logger logger = LoggerFactory.getLogger(DbBagGoodsService.class);
  
  private static volatile DbBagGoodsService instance;
  
  @Autowired
  CrossServerConfig crossServerConfig;
  
  @Autowired
  private Dao dao;
  
  public static DbBagGoodsService getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("db-bag-goods-save-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<BagGoods> queue = (BlockingQueue<BagGoods>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(BagGoods entity) {
    this.queue.add(entity);
  }
  
  public int size() {
    return this.queue.size();
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (DbBagGoodsService.this.run.get()) {
        BagGoods entity = null;
        try {
          entity = DbBagGoodsService.this.queue.take();
          StopWatch stopWatch = new StopWatch();
          stopWatch.start();
          DbBagGoodsService.this.saveToDb(entity);
          stopWatch.stop();
          if (stopWatch.getTime() > 500L) {
            LoggerUtils.info("背包物品数据持久化消耗毫秒：" + stopWatch.getTime(), new Object[0]);
            LoggerUtils.info("背包物品数据持久化队列长度：" + DbBagGoodsService.this.queue.size(), new Object[0]);
          } 
        } catch (Exception e) {
          DbBagGoodsService.this.logger.error("背包物品存档异常", e);
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(BagGoods entity) {
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
    LoggerUtils.error("[DbBagGoods] 执行全部命令后关闭", new Object[0]);
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<BagGoods> it = this.queue.iterator();
      while (it.hasNext()) {
        BagGoods obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
