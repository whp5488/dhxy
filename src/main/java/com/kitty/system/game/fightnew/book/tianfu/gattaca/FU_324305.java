package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324305 extends TianFuProcessor
{
    public FU_324305() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (RACE.RACE_MO.getValue() == effector.getRace() || RACE.RACE_LONG.getValue() == effector.getRace()) {
            if (!effector.isPlayer()) {
                return null;
            }
            DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324305);
            if (tianFuObject == null) {
                return null;
            }
            Integer mp = (Integer)args[0];
            if (mp == null || (int)mp <= 0) {
                return null;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324305);
            double percenta = 3.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int alterMp = (int)((double)(int)mp * percenta);
            NutMap map = NutMap.NEW();
            map.setv("alterMp", Integer.valueOf(-alterMp));
            skillRecord.setT((long)(int)effector.getId());
            return map;
        }
        else {
            return null;
        }
    }
}
