
package com.kitty.system.game.fightnew.reward;

import java.util.HashMap;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.bean.Fight;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import java.util.Map;
import org.slf4j.Logger;

public abstract class FightRewardHandler
{
    protected final Logger logger;
    private static final Map<Integer, FightRewardHandler> handlers;
    
    public FightRewardHandler() {
        this.logger = LoggerFactory.getLogger(FightRewardHandler.class);
    }
    
    @PostConstruct
    public void init() {
        FightRewardHandler.handlers.put(this.getType(), this);
    }
    
    public void putHndlers(final FightRewardHandler fightRewardHandler) {
        FightRewardHandler.handlers.put(this.getType(), fightRewardHandler);
    }
    
    public static FightRewardHandler queryHandler(final int type) {
        return FightRewardHandler.handlers.get(type);
    }
    
    public abstract void sendReward(final Fight p0, final Role p1, final Pet p2);
    
    public abstract void fightFailed(final Fight p0, final Role p1);
    
    public abstract void flee(final Fight p0, final Role p1);
    
    public abstract int getType();
    
    static {
        handlers = new HashMap<Integer, FightRewardHandler>();
    }
}
