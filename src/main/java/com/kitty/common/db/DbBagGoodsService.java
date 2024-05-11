
package com.kitty.common.db;

import org.apache.commons.lang3.time.StopWatch;
import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DbBagGoodsService
{
    Logger logger;
    private static volatile DbBagGoodsService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<BagGoods> queue;
    private final AtomicBoolean run;
    
    public DbBagGoodsService() {
        this.logger = LoggerFactory.getLogger(DbBagGoodsService.class);
        this.queue = new BlockingUniqueQueue<BagGoods>();
        this.run = new AtomicBoolean(true);
    }
    
    public static DbBagGoodsService getInstance() {
        return DbBagGoodsService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-bag-goods-save-service").newThread(new Worker()).start();
        DbBagGoodsService.instance = this;
    }
    
    public void add2Queue(final BagGoods entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(final BagGoods entity) {
        entity.doBeforeSave();
        if (entity.isDelete()) {
            this.dao.delete((Object)entity);
        }
        else {
            this.dao.insertOrUpdate((Object)entity);
        }
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[DbBagGoods] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<BagGoods> it = this.queue.iterator();
            while (it.hasNext()) {
                final BagGoods obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (DbBagGoodsService.this.run.get()) {
                BagGoods entity = null;
                try {
                    entity = DbBagGoodsService.this.queue.take();
                    final StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    DbBagGoodsService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() <= 500L) {
                        continue;
                    }
                    LoggerUtils.info("背包物品数据持久化消耗毫秒：" + stopWatch.getTime());
                    LoggerUtils.info("背包物品数据持久化队列长度：" + DbBagGoodsService.this.queue.size());
                }
                catch (final Exception e) {
                    DbBagGoodsService.this.logger.error("背包物品存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
