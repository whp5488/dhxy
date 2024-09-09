package com.kitty.system.game.fightnew.factory;

import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import java.util.HashMap;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.logs.LoggerFunction;
import java.util.Map;
import org.slf4j.Logger;

public abstract class FightFactory
{
    protected Logger logger;
    private static Map<Integer, FightFactory> factorys;
    
    public FightFactory() {
        this.logger = LoggerFunction.FIGHT.getLogger();
    }
    
    public abstract Fight create(int mapId, Object... params);
    
    public static FightFactory getFightFactory(int type) {
        return (FightFactory)FightFactory.factorys.get(Integer.valueOf(type));
    }
    
    static {
        (FightFactory.factorys = new HashMap()).put(Integer.valueOf(FIGHT_TYPE.MONSTER.getValue()), new WildMonsterFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.PLAY.getValue()), new TeamPkFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.HG_FIGHT.getValue()), new TeamPkFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.TASK.getValue()), new BossFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.STAR.getValue()), new BossFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.SUPER_BOSS.getValue()), new BossFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.NIAN.getValue()), new BossFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.BURNING_HOT.getValue()), new BossFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.SEAL_DEMON.getValue()), new BossFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.THREE_SCOPE.getValue()), new BossFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.PARTY_BANDIT.getValue()), new BossFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.AT_WILL_PK.getValue()), new TeamPkFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.GANG_WARS.getValue()), new TeamPkFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.WATER_PK.getValue()), new TeamPkFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.CA_PK.getValue()), new TeamPkFightFactory());
        FightFactory.factorys.put(Integer.valueOf(FIGHT_TYPE.FORCE_PK.getValue()), new TeamPkFightFactory());
    }
}
