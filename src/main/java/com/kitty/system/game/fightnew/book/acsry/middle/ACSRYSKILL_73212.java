package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73212 extends AcsrySkillProcessor
{
    public ACSRYSKILL_73212() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            FightObject recordTarget = null;
            for (FightObject obj : fight.getMySide((int)fightObject.getId())) {
                if (obj.getStatusValue(1010) > 0) {
                    if (obj.getMaxMana() <= obj.getCurrMana()) {
                        continue;
                    }
                    int unitId = obj.getStatusSubMap().getInt("1010target");
                    if ((int)fightObject.getId() != unitId) {
                        continue;
                    }
                    if (recordTarget == null) {
                        recordTarget = obj;
                    }
                    int oldMana = obj.getCurrMana();
                    int addMana = (int)((double)obj.getMaxMana() * 0.1);
                    if (addMana + oldMana > obj.getMaxMana()) {
                        addMana = obj.getMaxMana() - obj.getCurrMana();
                    }
                    ActionParamUtils.changeMana(fight, fightObject, obj, 7, 300999, 2, addMana);
                }
                else {
                    continue;
                }
            }
            if (recordTarget != null) {
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 73212);
                skillRecord.setT((long)(int)recordTarget.getId());
            }
        }
        return;
    }
}
