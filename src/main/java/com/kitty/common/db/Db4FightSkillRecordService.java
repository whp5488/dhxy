
package com.kitty.common.db;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.fightRecord.domain.FightSkillRecord;
import java.util.concurrent.BlockingQueue;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.kitty.common.start.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Component;

@Component
public class Db4FightSkillRecordService
{
    private static volatile Db4FightSkillRecordService instance;
    @Autowired
    private Dao dao;
    @Autowired
    private ServerService serverService;
    @Autowired
    private MongoTemplate mongoTemplate;
    private BlockingQueue<FightSkillRecord> queue;
    private final AtomicBoolean run;
    
    public Db4FightSkillRecordService() {
        this.queue = new BlockingUniqueQueue<FightSkillRecord>();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4FightSkillRecordService getInstance() {
        return Db4FightSkillRecordService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-fight-skill-record-service").newThread(new Worker()).start();
        Db4FightSkillRecordService.instance = this;
    }
    
    public void add2Queue(final FightSkillRecord record) {
        if (record == null) {
            return;
        }
        record.setMarking(this.serverService.getMarking());
        this.queue.add(record);
    }
    
    private void saveToDb(final FightSkillRecord record) {
        record.doBeforeSave();
        this.mongoTemplate.insert((Object)record);
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[Db4FightSkillRecord] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<FightSkillRecord> it = this.queue.iterator();
            while (it.hasNext()) {
                final FightSkillRecord next = it.next();
                it.remove();
                this.saveToDb(next);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4FightSkillRecordService.this.run.get()) {
                FightSkillRecord record = null;
                try {
                    record = Db4FightSkillRecordService.this.queue.take();
                    Db4FightSkillRecordService.this.saveToDb(record);
                }
                catch (final Exception e) {
                    LoggerUtils.error("公共数据(排查玩家数据)持久化队列", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
