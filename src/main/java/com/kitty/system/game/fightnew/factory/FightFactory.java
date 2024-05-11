
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
    
    public abstract Fight create(final int p0, final Object... p1);
    
    public static FightFactory getFightFactory(final int type) {
        return FightFactory.factorys.get(type);
    }
    
    static {
        (FightFactory.factorys = new HashMap<Integer, FightFactory>()).put(FIGHT_TYPE.MONSTER.getValue(), new WildMonsterFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.PLAY.getValue(), new TeamPkFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.HG_FIGHT.getValue(), new TeamPkFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.TASK.getValue(), new BossFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.STAR.getValue(), new BossFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.SUPER_BOSS.getValue(), new BossFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.NIAN.getValue(), new BossFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.BURNING_HOT.getValue(), new BossFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.SEAL_DEMON.getValue(), new BossFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.THREE_SCOPE.getValue(), new BossFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.PARTY_BANDIT.getValue(), new BossFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.AT_WILL_PK.getValue(), new TeamPkFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.GANG_WARS.getValue(), new TeamPkFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.WATER_PK.getValue(), new TeamPkFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.CA_PK.getValue(), new TeamPkFightFactory());
        FightFactory.factorys.put(FIGHT_TYPE.FORCE_PK.getValue(), new TeamPkFightFactory());
    }
}
