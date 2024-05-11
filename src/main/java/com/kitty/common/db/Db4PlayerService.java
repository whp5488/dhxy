
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
import com.kitty.system.game.role.domain.Role;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import com.kitty.system.game.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Db4PlayerService
{
    Logger logger;
    private static volatile Db4PlayerService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    PlayerService playerService;
    @Autowired
    private Dao dao;
    private BlockingQueue<Role> queue;
    private final AtomicBoolean run;
    
    public Db4PlayerService() {
        this.logger = LoggerFactory.getLogger(Db4PlayerService.class);
        this.queue = new BlockingUniqueQueue<Role>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4PlayerService getInstance() {
        return Db4PlayerService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-player-save-service").newThread(new Worker()).start();
        Db4PlayerService.instance = this;
    }
    
    public void add2Queue(final Role entity) {
        this.queue.add(entity);
        this.playerService.saveToCache(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(final Role entity) {
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
        LoggerUtils.error("[db4Player] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<Role> it = this.queue.iterator();
            while (it.hasNext()) {
                final Role player = it.next();
                it.remove();
                this.saveToDb(player);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4PlayerService.this.run.get()) {
                Role entity = null;
                try {
                    entity = Db4PlayerService.this.queue.take();
                    final StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    Db4PlayerService.this.saveToDb(entity);
                    stopWatch.stop();
                    if (stopWatch.getTime() <= 500L) {
                        continue;
                    }
                    LoggerUtils.info("玩家数据持久化消耗毫秒：" + stopWatch.getTime());
                    LoggerUtils.info("玩家数据持久化队列长度：" + Db4PlayerService.this.queue.size());
                }
                catch (final Exception e) {
                    Db4PlayerService.this.logger.error("存档异常角色编号：{}，SID：{}", entity.getRoleId(), entity.getSid());
                    e.printStackTrace();
                }
            }
        }
    }
}
