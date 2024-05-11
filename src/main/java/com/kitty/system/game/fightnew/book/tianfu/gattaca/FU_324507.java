
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
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (effector.getRace() == RACE.RACE_LONG.getValue()) {
            final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324507);
            if (tianFuObject == null) {
                return null;
            }
            final double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final boolean flag = SkillAi.canAffectOnPro(percenta);
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324507);
            if (flag) {
                final int time = effector.getSkillCoolMap().getInt(String.valueOf(30041));
                if (time > 0) {
                    effector.getSkillCoolMap().setv(String.valueOf(30041), (Object)(time - 1));
                    skillRecord.setT(effector.getId());
                }
            }
        }
        return null;
    }
}
