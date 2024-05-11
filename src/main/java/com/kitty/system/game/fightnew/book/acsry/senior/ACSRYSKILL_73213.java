
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73213 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            double baseLifePCT = 0.1;
            final Role role = fightObject.getRole();
            final Integer mj = role.getMj();
            baseLifePCT += mj / 200 * 0.01;
            FightObject recordTarget = null;
            for (final FightObject obj : fight.getMySide(fightObject.getId())) {
                if (obj.getStatusValue(1010) > 0) {
                    if (obj.getMaxMana() <= obj.getCurrMana()) {
                        continue;
                    }
                    final int unitId = obj.getStatusSubMap().getInt("1010target");
                    if (fightObject.getId() != unitId) {
                        continue;
                    }
                    if (recordTarget == null) {
                        recordTarget = obj;
                    }
                    final int oldMana = obj.getCurrMana();
                    int addMana = (int)(obj.getMaxMana() * baseLifePCT);
                    if (addMana + oldMana > obj.getMaxMana()) {
                        addMana = obj.getMaxMana() - obj.getCurrMana();
                    }
                    ActionParamUtils.changeMana(fight, fightObject, obj, 7, 300999, 2, addMana);
                }
            }
            if (recordTarget != null) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 73213);
                skillRecord.setT(recordTarget.getId());
            }
        }
    }
}
