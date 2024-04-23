package com.kitty.common.db;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongoRoleThread {
  @Autowired
  private MongoTemplate mongoTemplate;
  
  private static volatile MongoRoleThread instance;
  
  public static MongoRoleThread getInstance() {
    return instance;
  }
  
  @PostConstruct
  private void init() {
    (new NamedThreadFactory("mongo-role-service")).newThread(new Worker()).start();
    instance = this;
  }
  
  private BlockingQueue<Role> queue = (BlockingQueue<Role>)new BlockingUniqueQueue();
  
  private final AtomicBoolean run = new AtomicBoolean(true);
  
  public void add2Queue(Role entity) {
    this.queue.add(entity);
  }
  
  private class Worker implements Runnable {
    private Worker() {}
    
    public void run() {
      while (MongoRoleThread.this.run.get()) {
        try {
          Role take = MongoRoleThread.this.queue.take();
          MongoRoleThread.this.saveToDb(take);
        } catch (Exception e) {
          e.printStackTrace();
        } 
      } 
    }
  }
  
  private void saveToDb(Role entity) {
    Query query = new Query();
    query.addCriteria((CriteriaDefinition)Criteria.where("id").is(entity.getId()));
    Update update = new Update();
    update.set("roleId", entity.getRoleId());
    update.set("sid", entity.getSid());
    update.set("name", entity.getName());
    update.set("sex", entity.getSex());
    update.set("race", entity.getRace());
    update.set("level", entity.getLevel());
    update.set("relive", entity.getRelive());
    update.set("exp", Long.valueOf(entity.getExp()));
    update.set("saveLife", entity.getSaveLife());
    update.set("saveMana", entity.getSaveMana());
    update.set("gold", entity.getGold());
    update.set("tc", Integer.valueOf(entity.getTc()));
    update.set("totalGold", Long.valueOf(entity.getTotalGold()));
    update.set("silver", entity.getSilver());
    update.set("coin", entity.getCoin());
    update.set("icon", Integer.valueOf(entity.getIcon()));
    update.set("weaponIcon", Integer.valueOf(entity.getWeaponIcon()));
    update.set("masquerade", Integer.valueOf(entity.getMasquerade()));
    update.set("cardExpireTime", Integer.valueOf(entity.getCardExpireTime()));
    update.set("color", entity.getColor());
    update.set("serial", entity.getSerial());
    update.set("vip", entity.getVip());
    update.set("currTitle", Integer.valueOf(entity.getCurrTitle()));
    update.set("gg", entity.getGg());
    update.set("lx", entity.getLx());
    update.set("ll", entity.getLl());
    update.set("mj", entity.getMj());
    update.set("fpoint", entity.getFaction());
    update.set("currLife", entity.getCurrLife());
    update.set("maxLife", entity.getMaxLife());
    update.set("currMana", entity.getMaxMana());
    update.set("maxMana", entity.getMaxMana());
    update.set("faction", entity.getFaction());
    update.set("safePwd", entity.getSafePwd());
    update.set("dailyFiveResetTime", entity.getDailyFiveResetTime());
    update.set("weeklyResetTime", Long.valueOf(entity.getWeeklyResetTime()));
    update.set("monthResetTime", Long.valueOf(entity.getMonthResetTime()));
    update.set("lockTime", entity.getLockTime());
    update.set("enterTime", entity.getEnterTime());
    update.set("ofLineTime", entity.getOfLineTime());
    update.set("skills", entity.getSkills());
    update.set("offlineNotice", entity.getOfflineNotice());
    update.set("pos", entity.getPos());
    this.mongoTemplate.upsert(query, update, Role.class, "role");
  }
  
  @PreDestroy
  public void shutDown() {
    this.run.getAndSet(false);
    while (!this.queue.isEmpty())
      saveAllBeforeShutDown(); 
    LoggerUtils.error("[MongoRole] 执行全部命令后关闭", new Object[0]);
  }
  
  private void saveAllBeforeShutDown() {
    while (!this.queue.isEmpty()) {
      Iterator<Role> it = this.queue.iterator();
      while (it.hasNext()) {
        Role obj = it.next();
        it.remove();
        saveToDb(obj);
      } 
    } 
  }
}
