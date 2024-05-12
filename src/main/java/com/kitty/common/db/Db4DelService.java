
package com.kitty.common.db;

import com.kitty.logs.LoggerUtils;
import com.kitty.common.utils.spring.SpringUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.start.ServerService;
import org.springframework.stereotype.Component;

@Component
public class Db4DelService
{
    @Autowired
    ServerService serverService;
    Logger logger;
    private static volatile Db4DelService instance;
    @Autowired
    private Dao dao;
    private BlockingQueue<BaseEntity> queue;
    private final AtomicBoolean run;
    
    public Db4DelService() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.queue = new BlockingUniqueQueue<BaseEntity>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4DelService getInstance() {
        return Db4DelService.instance;
    }
    
    @PostConstruct
    public void init() {
        for (int i = 0; i < 32; ++i) {
            new NamedThreadFactory("db-common-save-service-" + i).newThread(new Worker()).start();
        }
        this.logger.info("初始化线程32条");
        Db4DelService.instance = this;
    }
    
    public void delQueue(final BaseEntity entity) {
        if (entity == null) {
            return;
        }
        this.queue.add(entity);
    }
    
    public void delToDb(final BaseEntity entity) {
        this.dao.delete(entity);
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4DelService.this.run.get()) {
                BaseEntity entity = null;
                try {
                    entity = Db4DelService.this.queue.take();
                    SpringUtils.getBean(Db4DelService.class).delToDb(entity);
                }
                catch (final Exception e) {
                    LoggerUtils.error("", e);
                    e.printStackTrace();
                }
                Db4DelService.this.logger.error("队列剩余数量=={}", Db4DelService.this.queue.size());
            }
        }
    }
}
