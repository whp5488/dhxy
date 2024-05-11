
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
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

public class FU_323104 extends TianFuProcessor
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
        final FightObject host = fight.queryFightObject(effector.getHostId());
        final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323104);
        if (tianFuObject == null) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 323104);
        final boolean flag = SkillAi.canAffectOnPro(0.1);
        if (flag) {
            final Integer hurt = (Integer)args[0];
            if (hurt == null || hurt <= 0) {
                return;
            }
            final double percentb = 1.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int addLife = (int)(hurt * percentb);
            if (addLife > 0) {
                if (addLife + effector.getCurrLife() > effector.getMaxLife()) {
                    addLife = effector.getMaxLife() - effector.getCurrLife();
                }
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId(), true);
                PassiveSkillUtils.excuteChangeLifeAuto(fight, effector, actionDetailParam, targetParam, addLife);
                skillRecord.setT(effector.getId());
            }
        }
    }
}
