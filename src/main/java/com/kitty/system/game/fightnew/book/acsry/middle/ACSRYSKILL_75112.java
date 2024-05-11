
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75112 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        final int oldMana = fightObject.getCurrMana();
        int addMana = (int)(fightObject.getMaxMana() * 0.05);
        if (oldMana + addMana > fightObject.getMaxMana()) {
            addMana = fightObject.getMaxMana() - fightObject.getCurrMana();
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 75112);
        skillRecord.setT(fightObject.getId());
        ActionParamUtils.changeMana(fight, fightObject, fightObject, 7, 300999, 2, addMana);
    }
}
