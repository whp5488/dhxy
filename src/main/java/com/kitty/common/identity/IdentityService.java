package com.kitty.common.identity;

import com.kitty.common.db.Db4CommonService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.ServerConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityService {
  @Autowired
  private Dao dao;
  
  private Map<Integer, IdentityEnt> map = new HashMap<>();
  
  private AtomicInteger defaultFactory = new AtomicInteger(1);
  
  public void init() {
    for (IdentityType kind : IdentityType.values()) {
      int type1 = kind.getType();
      if (kind.isSave()) {
        IdentityEnt ent = (IdentityEnt)this.dao.fetch(IdentityEnt.class, (Condition)Cnd.where("type", "=", Integer.valueOf(type1)));
        if (ent == null) {
          int initSeed = createInitSeed(kind);
          if (initSeed > 0) {
            ent = IdentityEnt.valueOf(type1, initSeed);
            this.dao.insertOrUpdate(ent);
          } 
        } 
        this.map.put(ent.getType(), ent);
      } 
    } 
  }
  
  private int createInitSeed(IdentityType kind) {
    ServerConfig config = (ServerConfig)SpringUtils.getBean(ServerConfig.class);
    if (config.getSerial() > 0)
      return kind.getValue() + (config.getSerial() - 1) * kind.getAddValue(); 
    return -1;
  }
  
  public int getNextId(IdentityType idType) {
    if (!idType.isSave())
      return this.defaultFactory.getAndIncrement(); 
    int type = idType.getType();
    IdentityEnt ent = this.map.get(type);
    int nextId = ent.getNextId();
    Db4CommonService.getInstance().add2Queue(ent);
    return nextId;
  }
}
