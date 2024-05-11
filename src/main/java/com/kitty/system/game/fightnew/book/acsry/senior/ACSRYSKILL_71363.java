
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.List;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71363 extends AcsrySkillProcessor
{
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        List<Integer> clearStatusList = null;
        try {
            clearStatusList = (List)args[2];
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (clearStatusList != null && clearStatusList.contains(1004)) {
            final double percent = 0.25;
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 71363);
            final boolean flagF = SkillAi.canAffectOnPro(percent);
            if (flagF) {
                target.setStatus(1003, 1);
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
                if (targetParam != null) {
                    targetParam.getStatusMap().put(1003, 1);
                    skillRecord.setT(target.getId());
                }
                return;
            }
            final boolean flagC = SkillAi.canAffectOnPro(percent);
            if (flagC) {
                target.setStatus(1002, 1);
                final FightObjectParam targetParam2 = actionDetailParam.gainTargetParam(target.getId(), false);
                if (targetParam2 != null) {
                    targetParam2.getStatusMap().put(1002, 1);
                    skillRecord.setT(target.getId());
                }
            }
        }
    }
}
