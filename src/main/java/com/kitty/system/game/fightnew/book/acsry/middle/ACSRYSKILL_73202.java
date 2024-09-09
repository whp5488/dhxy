package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73202 extends AcsrySkillProcessor
{
    public ACSRYSKILL_73202() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList.contains(Integer.valueOf(73203))) {
                return;
            }
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
                    int addLife = (int)((double)obj.getMaxLife() * 0.1);
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
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 73202);
                skillRecord.setT((long)(int)recordTarget.getId());
            }
        }
        return;
    }
}
