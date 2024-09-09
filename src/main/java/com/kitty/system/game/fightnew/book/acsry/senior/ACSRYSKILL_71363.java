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
    public ACSRYSKILL_71363() {
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        List<Integer> clearStatusList = null;
        try {
            clearStatusList = (List)args[2];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (clearStatusList != null && clearStatusList.contains(Integer.valueOf(1004))) {
            double percent = 0.25;
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 71363);
            boolean flagF = SkillAi.canAffectOnPro(percent);
            if (flagF) {
                target.setStatus(1003, 1);
                FightObjectParam targetParam2 = actionDetailParam.gainTargetParam((int)target.getId(), false);
                if (targetParam2 != null) {
                    targetParam2.getStatusMap().put(Integer.valueOf(1003), Integer.valueOf(1));
                    skillRecord.setT((long)(int)target.getId());
                }
                return;
            }
            else {
                boolean flagC = SkillAi.canAffectOnPro(percent);
                if (flagC) {
                    target.setStatus(1002, 1);
                    FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), false);
                    if (targetParam != null) {
                        targetParam.getStatusMap().put(Integer.valueOf(1002), Integer.valueOf(1));
                        skillRecord.setT((long)(int)target.getId());
                    }
                    return;
                }
            }
        }
    }
}
