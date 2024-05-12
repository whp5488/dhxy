
package com.kitty.common.identity;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.ServerConfig;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class IdentityService
{
    @Autowired
    private Dao dao;
    private Map<Integer, IdentityEnt> map;
    private AtomicInteger defaultFactory;
    
    public IdentityService() {
        this.map = new HashMap<>();
        this.defaultFactory = new AtomicInteger(1);
    }
    
    public void init() {
        for (final IdentityType kind : IdentityType.values()) {
            final int type1 = kind.getType();
            if (kind.isSave()) {
                IdentityEnt ent = (IdentityEnt)this.dao.fetch(IdentityEnt.class, Cnd.where("type", "=", type1));
                if (ent == null) {
                    final int initSeed = this.createInitSeed(kind);
                    if (initSeed > 0) {
                        ent = IdentityEnt.valueOf(type1, initSeed);
                        this.dao.insertOrUpdate((Object)ent);
                    }
                }
                this.map.put(ent.getType(), ent);
            }
        }
    }
    
    private int createInitSeed(final IdentityType kind) {
        final ServerConfig config = SpringUtils.getBean(ServerConfig.class);
        if (config.getSerial() > 0) {
            return kind.getValue() + (config.getSerial() - 1) * kind.getAddValue();
        }
        return -1;
    }
    
    public int getNextId(final IdentityType idType) {
        if (!idType.isSave()) {
            return this.defaultFactory.getAndIncrement();
        }
        final int type = idType.getType();
        final IdentityEnt ent = this.map.get(type);
        final int nextId = ent.getNextId();
        Db4CommonService.getInstance().add2Queue(ent);
        return nextId;
    }
}
