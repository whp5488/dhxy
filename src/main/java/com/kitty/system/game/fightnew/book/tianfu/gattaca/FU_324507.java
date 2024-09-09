package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324507 extends TianFuProcessor
{
    public FU_324507() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (effector.getRace() == RACE.RACE_LONG.getValue()) {
            DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324507);
            if (tianFuObject == null) {
                return null;
            }
            double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flag = SkillAi.canAffectOnPro(percenta);
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324507);
            if (flag) {
                int time = effector.getSkillCoolMap().getInt(String.valueOf(30041));
                if (time > 0) {
                    effector.getSkillCoolMap().setv(String.valueOf(30041), Integer.valueOf(time - 1));
                    skillRecord.setT((long)(int)effector.getId());
                }
            }
        }
        return null;
    }
}
