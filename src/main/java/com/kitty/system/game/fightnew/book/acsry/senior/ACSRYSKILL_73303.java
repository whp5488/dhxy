package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.AcsrySkillInsideUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73303 extends AcsrySkillProcessor
{
    public ACSRYSKILL_73303() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            double percent = 0.1;
            Role role = fightObject.getRole();
            percent += (double)((int)role.getMj() / 200) * 0.01;
            FightObject recordTarget = null;
            for (FightObject obj : fight.getMySide((int)fightObject.getId())) {
                if (obj.getStatusValue(1009) > 0) {
                    int unitId = obj.getStatusSubMap().getInt("1009target");
                    if ((int)fightObject.getId() != unitId) {
                        continue;
                    }
                    if (SkillAi.canAffectOnPro(percent)) {
                        if (obj.isFrozen()) {
                            AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1003);
                            if (recordTarget == null) {
                                recordTarget = obj;
                            }
                        }
                        if (obj.isForgotten()) {
                            AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1006);
                            if (recordTarget == null) {
                                recordTarget = obj;
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (recordTarget != null) {
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 73303);
                skillRecord.setT((long)(int)recordTarget.getId());
            }
        }
        return;
    }
}
