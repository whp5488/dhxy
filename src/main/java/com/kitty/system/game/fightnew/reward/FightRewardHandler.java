package com.kitty.system.game.fightnew.reward;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FightRewardHandler {
  protected final Logger logger = LoggerFactory.getLogger(FightRewardHandler.class);
  
  private static final Map<Integer, FightRewardHandler> handlers = new HashMap<>();
  
  @PostConstruct
  public void init() {
    handlers.put(Integer.valueOf(getType()), this);
  }
  
  public void putHndlers(FightRewardHandler fightRewardHandler) {
    handlers.put(Integer.valueOf(getType()), fightRewardHandler);
  }
  
  public static FightRewardHandler queryHandler(int type) {
    return handlers.get(Integer.valueOf(type));
  }
  
  public abstract void sendReward(Fight paramFight, Role paramRole, Pet paramPet);
  
  public abstract void fightFailed(Fight paramFight, Role paramRole);
  
  public abstract void flee(Fight paramFight, Role paramRole);
  
  public abstract int getType();
}
