package com.kitty.system.game.fightnew.factory;

import com.kitty.logs.LoggerFunction;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

public abstract class FightFactory {
  protected Logger logger = LoggerFunction.FIGHT.getLogger();
  
  private static Map<Integer, FightFactory> factorys = new HashMap<>();
  
  static {
    factorys.put(Integer.valueOf(FIGHT_TYPE.MONSTER.getValue()), new WildMonsterFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.PLAY.getValue()), new TeamPkFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.HG_FIGHT.getValue()), new TeamPkFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.TASK.getValue()), new BossFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.STAR.getValue()), new BossFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.SUPER_BOSS.getValue()), new BossFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.NIAN.getValue()), new BossFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.BURNING_HOT.getValue()), new BossFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.SEAL_DEMON.getValue()), new BossFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.THREE_SCOPE.getValue()), new BossFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.PARTY_BANDIT.getValue()), new BossFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.AT_WILL_PK.getValue()), new TeamPkFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.GANG_WARS.getValue()), new TeamPkFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.WATER_PK.getValue()), new TeamPkFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.CA_PK.getValue()), new TeamPkFightFactory());
    factorys.put(Integer.valueOf(FIGHT_TYPE.FORCE_PK.getValue()), new TeamPkFightFactory());
  }
  
  public static FightFactory getFightFactory(int type) {
    return factorys.get(Integer.valueOf(type));
  }
  
  public abstract Fight create(int paramInt, Object... paramVarArgs);
}
