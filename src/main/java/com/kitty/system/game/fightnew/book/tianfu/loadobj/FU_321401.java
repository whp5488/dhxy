
package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321401 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (!target.isPlayer()) {
            return;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321401);
        if (tianFuObject == null) {
            return;
        }
        if (target.getStatusValue(1003) > 0) {
            return;
        }
        final Integer hurt = (Integer)args[1];
        if (hurt == null || hurt <= 0) {
            return;
        }
        if (target.getStatusValue(1009) <= 0) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321401);
            if (target.getSkillCoolMap().getInt(String.valueOf(321401)) > 0) {
                return;
            }
            final double percenta = 7.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
            if (!flagTrigger) {
                return;
            }
            final int time = TianFuUtils.defUp(target, 3);
            if (time <= 0) {
                return;
            }
            target.getStatusMap().setv(String.valueOf(1009), (Object)time);
            target.getSkillCoolMap().setv(String.valueOf(321401), (Object)8);
            final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
            if (targetParam != null) {
                targetParam.getStatusMap().put(1009, time);
                skillRecord.setT(target.getId());
            }
        }
    }
}
