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
        FightRewardHandler.handlers.put(Integer.valueOf(this.getType()), this);
    }
    
    public void putHndlers(FightRewardHandler fightRewardHandler) {
        FightRewardHandler.handlers.put(Integer.valueOf(this.getType()), fightRewardHandler);
    }
    
    public static FightRewardHandler queryHandler(int type) {
        return (FightRewardHandler)FightRewardHandler.handlers.get(Integer.valueOf(type));
    }
    
    public abstract void sendReward(Fight fight, Role role, Pet pet);
    
    public abstract void fightFailed(Fight fight, Role role);
    
    public abstract void flee(Fight fight, Role role);
    
    public abstract int getType();
    
    static {
        handlers = new HashMap();
    }
}
