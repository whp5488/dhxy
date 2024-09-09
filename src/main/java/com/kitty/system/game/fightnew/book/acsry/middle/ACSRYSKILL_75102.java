package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75102 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75102() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        int oldLife = fightObject.getCurrLife();
        int addLife = (int)((double)fightObject.getMaxLife() * 0.05);
        if (oldLife + addLife > fightObject.getMaxLife()) {
            addLife = fightObject.getMaxLife() - fightObject.getCurrLife();
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 75102);
        skillRecord.setT((long)(int)fightObject.getId());
        ActionParamUtils.changeLife(fight, fightObject, fightObject, 7, 300999, 2, addLife, false);
        return;
    }
}
