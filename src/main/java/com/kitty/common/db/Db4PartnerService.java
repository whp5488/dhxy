package com.kitty.common.db;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.partner.domain.Partner;
import java.util.concurrent.BlockingQueue;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cross.CrossServerConfig;
import org.springframework.stereotype.Component;

@Component
public class Db4PartnerService
{
    private static volatile Db4PartnerService instance;
    @Autowired
    CrossServerConfig crossServerConfig;
    @Autowired
    private Dao dao;
    private BlockingQueue<Partner> queue;
    private final AtomicBoolean run;
    
    public Db4PartnerService() {
        this.queue = new BlockingUniqueQueue();
        this.run = new AtomicBoolean(true);
    }
    
    public static Db4PartnerService getInstance() {
        return Db4PartnerService.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("db-partner-save-service").newThread(new Db4PartnerService.Worker()).start();
        Db4PartnerService.instance = this;
    }
    
    public void add2Queue(Partner entity) {
        this.queue.add(entity);
    }
    
    public int size() {
        return this.queue.size();
    }
    
    private void saveToDb(Partner entity) {
        entity.doBeforeSave();
        if (entity.isDelete()) {
            this.dao.delete(entity);
        }
        else {
            this.dao.insertOrUpdate(entity);
        }
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[db4Partner] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            Iterator<Partner> it = this.queue.iterator();
            while (it.hasNext()) {
                Partner partner = (Partner)it.next();
                it.remove();
                this.saveToDb(partner);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (Db4PartnerService.this.run.get()) {
                Partner entity = null;
                try {
                    entity = (Partner)Db4PartnerService.this.queue.take();
                    Db4PartnerService.this.saveToDb(entity);
                }
                catch (Exception e) {
                    LoggerUtils.error("存档异常", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
