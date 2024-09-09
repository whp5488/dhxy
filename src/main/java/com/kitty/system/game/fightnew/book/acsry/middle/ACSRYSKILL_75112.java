package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75112 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75112() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        int oldMana = fightObject.getCurrMana();
        int addMana = (int)((double)fightObject.getMaxMana() * 0.05);
        if (oldMana + addMana > fightObject.getMaxMana()) {
            addMana = fightObject.getMaxMana() - fightObject.getCurrMana();
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 75112);
        skillRecord.setT((long)(int)fightObject.getId());
        ActionParamUtils.changeMana(fight, fightObject, fightObject, 7, 300999, 2, addMana);
        return;
    }
}
