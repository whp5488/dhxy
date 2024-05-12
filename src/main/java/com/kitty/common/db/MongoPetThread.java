
package com.kitty.common.db;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.pet.domain.Pet;
import java.util.concurrent.BlockingQueue;
import com.kitty.common.start.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoPetThread
{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ServerService serverService;
    private static volatile MongoPetThread instance;
    private BlockingQueue<Pet> queue;
    private final AtomicBoolean run;
    
    public MongoPetThread() {
        this.queue = new BlockingUniqueQueue<Pet>();
        this.run = new AtomicBoolean(true);
    }
    
    public static MongoPetThread getInstance() {
        return MongoPetThread.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("mongo-pet-service").newThread(new Worker()).start();
        MongoPetThread.instance = this;
    }
    
    public void add2Queue(final Pet entity) {
        this.queue.add(entity);
    }
    
    private void saveToDb(final Pet entity) {
        if (entity.delete) {
            final Query query = new Query(Criteria.where("id").is(entity.getId()));
            this.mongoTemplate.remove(query, Pet.class);
            return;
        }
        final Query query = new Query();
        query.addCriteria(Criteria.where("id").is(entity.getId()));
        final Update update = new Update();
        update.set("roleId", entity.getRoleId());
        update.set("petId", entity.getPetId());
        update.set("shape", entity.getShape());
        update.set("petType", entity.getPetType());
        update.set("petName", entity.getPetName());
        update.set("petLv", entity.getPetLv());
        update.set("rebirth", entity.getRebirth());
        update.set("fpoint", entity.getFpoint());
        update.set("exp", entity.getExp());
        update.set("petClose", entity.getPetClose());
        update.set("gg", entity.getGg());
        update.set("lx", entity.getLx());
        update.set("ll", entity.getLl());
        update.set("mj", entity.getMj());
        update.set("currLife", entity.getCurrLife());
        update.set("maxLife", entity.getMaxLife());
        update.set("currMana", entity.getMaxMana());
        update.set("maxMana", entity.getMaxMana());
        update.set("score", entity.getScore());
        update.set("growthDan", entity.getGrowthDan());
        update.set("isBase", entity.getIsBase());
        update.set("lyn", entity.getLyn());
        update.set("bone", entity.getBone());
        update.set("fly", entity.getFly());
        update.set("msgMap", entity.getMsgMap());
        update.set("closeLimitMap", entity.getCloseLimitMap());
        update.set("skills", entity.getSkills());
        update.set("correction", entity.getCorrection());
        update.set("autoAttrib", entity.getAutoAttrib());
        update.set("autoFightSet", entity.getAutoFightSet());
        update.set("darkBaseMap", entity.getDarkBaseMap());
        update.set("colorList", entity.getColorList());
        update.set("horse", entity.getHorse());
        update.set("bindTime", entity.getBindTime());
        update.set("petBase", entity.getPetBase());
        update.set("correctionInfo", entity.getCorrectionInfo());
        this.mongoTemplate.upsert(query, update, Pet.class, "pet");
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[MongoPet] 执行全部命令后关闭");
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<Pet> it = this.queue.iterator();
            while (it.hasNext()) {
                final Pet obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (MongoPetThread.this.run.get()) {
                try {
                    final Pet take = MongoPetThread.this.queue.take();
                    MongoPetThread.this.saveToDb(take);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
