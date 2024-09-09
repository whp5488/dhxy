package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73203 extends AcsrySkillProcessor
{
    public ACSRYSKILL_73203() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            double baseLifePCT = 0.1;
            Role role = fightObject.getRole();
            Integer mj = role.getMj();
            baseLifePCT += (double)((int)mj / 200) * 0.01;
            FightObject recordTarget = null;
            for (FightObject obj : fight.getMySide((int)fightObject.getId())) {
                if (obj.getStatusValue(1009) > 0) {
                    int unitId = obj.getStatusSubMap().getInt("1009target");
                    if ((int)fightObject.getId() != unitId) {
                        continue;
                    }
                    if (recordTarget == null) {
                        recordTarget = obj;
                    }
                    int oldLife = obj.getCurrLife();
                    int addLife = (int)((double)obj.getMaxLife() * baseLifePCT);
                    if (addLife + oldLife > obj.getMaxLife()) {
                        addLife = obj.getMaxLife() - obj.getCurrLife();
                    }
                    if (addLife > 0) {
                        ActionParamUtils.changeLife(fight, fightObject, obj, 7, 300999, 2, addLife, false);
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
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 73203);
                skillRecord.setT((long)(int)recordTarget.getId());
            }
        }
        return;
    }
    
    private void excute() {
    }
}
