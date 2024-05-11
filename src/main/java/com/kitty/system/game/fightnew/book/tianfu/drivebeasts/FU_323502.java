
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323502 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (effector.isPet() && effector.getComeRound() > 1) {
            final FightObject host = fight.queryFightObject(effector.getHostId());
            if (host.isFallDown()) {
                return null;
            }
            final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
            final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323502);
            if (tianFuObject == null) {
                return null;
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 323502);
            final double percenta = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final double percentb = 2.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flag = SkillAi.canAffectOnPro(percenta);
            if (flag) {
                final int time = TianFuUtils.defUp(effector, null);
                if (time <= 0) {
                    return null;
                }
                effector.getStatusMap().setv(String.valueOf(1009), (Object)time);
                ActionParamUtils.addStatus(fight, effector, effector, 7, 300999, 2, 1009, time);
            }
            flag = SkillAi.canAffectOnPro(percenta);
            if (flag) {
                final int time = TianFuUtils.speedUp(effector, null);
                if (time <= 0) {
                    return null;
                }
                effector.getStatusMap().setv(String.valueOf(1010), (Object)time);
                ActionParamUtils.addStatus(fight, effector, effector, 7, 300999, 2, 1010, time);
            }
            flag = SkillAi.canAffectOnPro(percentb);
            if (flag) {
                final int time = TianFuUtils.phyPowerUp(effector, null);
                if (time <= 0) {
                    return null;
                }
                effector.getStatusMap().setv(String.valueOf(1008), (Object)time);
                ActionParamUtils.addStatus(fight, effector, effector, 7, 300999, 2, 1008, time);
                skillRecord.setT(effector.getId());
            }
        }
        return null;
    }
}
