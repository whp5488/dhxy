
package com.kitty.common.db;

import java.util.Iterator;
import javax.annotation.PreDestroy;
import com.kitty.logs.LoggerUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.role.domain.Role;
import java.util.concurrent.BlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoRoleThread
{
    @Autowired
    private MongoTemplate mongoTemplate;
    private static volatile MongoRoleThread instance;
    private BlockingQueue<Role> queue;
    private final AtomicBoolean run;
    
    public MongoRoleThread() {
        this.queue = new BlockingUniqueQueue<Role>();
        this.run = new AtomicBoolean(true);
    }
    
    public static MongoRoleThread getInstance() {
        return MongoRoleThread.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("mongo-role-service").newThread(new Worker()).start();
        MongoRoleThread.instance = this;
    }
    
    public void add2Queue(final Role entity) {
        this.queue.add(entity);
    }
    
    private void saveToDb(final Role entity) {
        final Query query = new Query();
        query.addCriteria((CriteriaDefinition)Criteria.where("id").is((Object)entity.getId()));
        final Update update = new Update();
        update.set("roleId", (Object)entity.getRoleId());
        update.set("sid", (Object)entity.getSid());
        update.set("name", (Object)entity.getName());
        update.set("sex", (Object)entity.getSex());
        update.set("race", (Object)entity.getRace());
        update.set("level", (Object)entity.getLevel());
        update.set("relive", (Object)entity.getRelive());
        update.set("exp", (Object)entity.getExp());
        update.set("saveLife", (Object)entity.getSaveLife());
        update.set("saveMana", (Object)entity.getSaveMana());
        update.set("gold", (Object)entity.getGold());
        update.set("tc", (Object)entity.getTc());
        update.set("totalGold", (Object)entity.getTotalGold());
        update.set("silver", (Object)entity.getSilver());
        update.set("coin", (Object)entity.getCoin());
        update.set("icon", (Object)entity.getIcon());
        update.set("weaponIcon", (Object)entity.getWeaponIcon());
        update.set("masquerade", (Object)entity.getMasquerade());
        update.set("cardExpireTime", (Object)entity.getCardExpireTime());
        update.set("color", (Object)entity.getColor());
        update.set("serial", (Object)entity.getSerial());
        update.set("vip", (Object)entity.getVip());
        update.set("currTitle", (Object)entity.getCurrTitle());
        update.set("gg", (Object)entity.getGg());
        update.set("lx", (Object)entity.getLx());
        update.set("ll", (Object)entity.getLl());
        update.set("mj", (Object)entity.getMj());
        update.set("fpoint", (Object)entity.getFaction());
        update.set("currLife", (Object)entity.getCurrLife());
        update.set("maxLife", (Object)entity.getMaxLife());
        update.set("currMana", (Object)entity.getMaxMana());
        update.set("maxMana", (Object)entity.getMaxMana());
        update.set("faction", (Object)entity.getFaction());
        update.set("safePwd", (Object)entity.getSafePwd());
        update.set("dailyFiveResetTime", (Object)entity.getDailyFiveResetTime());
        update.set("weeklyResetTime", (Object)entity.getWeeklyResetTime());
        update.set("monthResetTime", (Object)entity.getMonthResetTime());
        update.set("lockTime", (Object)entity.getLockTime());
        update.set("enterTime", (Object)entity.getEnterTime());
        update.set("ofLineTime", (Object)entity.getOfLineTime());
        update.set("skills", (Object)entity.getSkills());
        update.set("offlineNotice", (Object)entity.getOfflineNotice());
        update.set("pos", (Object)entity.getPos());
        this.mongoTemplate.upsert(query, update, (Class)Role.class, "role");
    }
    
    @PreDestroy
    public void shutDown() {
        this.run.getAndSet(false);
        while (!this.queue.isEmpty()) {
            this.saveAllBeforeShutDown();
        }
        LoggerUtils.error("[MongoRole] 执行全部命令后关闭", new Object[0]);
    }
    
    private void saveAllBeforeShutDown() {
        while (!this.queue.isEmpty()) {
            final Iterator<Role> it = this.queue.iterator();
            while (it.hasNext()) {
                final Role obj = it.next();
                it.remove();
                this.saveToDb(obj);
            }
        }
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (MongoRoleThread.this.run.get()) {
                try {
                    final Role take = MongoRoleThread.this.queue.take();
                    MongoRoleThread.this.saveToDb(take);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
