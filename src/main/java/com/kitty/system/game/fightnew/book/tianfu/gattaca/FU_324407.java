
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.List;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324407 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final FightObject host = fight.queryFightObject(target.getHostId());
        final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324407);
        if (tianFuObject == null) {
            return null;
        }
        boolean statusFlag = false;
        final List<Integer> statusList = (List<Integer>)args[0];
        if (statusList.size() > 0 && statusList.contains(1008) && (target.getStatusValue(1008) <= 0 || target.isFallDown())) {
            statusFlag = true;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324407);
        if (target.isPet() && statusFlag) {
            final double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final boolean triggerFlag = SkillAi.canAffectOnPro(percenta);
            if (triggerFlag) {
                final List<FightObject> list = TianFuUtils.getFightObject(fight, target, 1008);
                if (list == null || list.size() <= 0) {
                    return null;
                }
                final FightObject myTarget = list.get(0);
                final int time = TianFuUtils.phyPowerUp(myTarget, target.getStatusValue(1008));
                myTarget.getStatusMap().setv(String.valueOf(1008), (Object)time);
                ActionParamUtils.addStatus(fight, target, myTarget, 7, 300999, 2, 1008, time);
                skillRecord.setT(target.getId());
            }
        }
        return null;
    }
}
