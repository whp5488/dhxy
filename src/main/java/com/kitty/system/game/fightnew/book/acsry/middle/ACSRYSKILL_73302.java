
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.AcsrySkillInsideUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73302 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            FightObject recordTarget = null;
            final double percent = 0.1;
            for (final FightObject obj : fight.getMySide(fightObject.getId())) {
                if (obj.getStatusValue(1009) > 0) {
                    final int unitId = obj.getStatusSubMap().getInt("1009target");
                    if (fightObject.getId() != unitId) {
                        continue;
                    }
                    if (!SkillAi.canAffectOnPro(percent)) {
                        continue;
                    }
                    if (obj.isFrozen()) {
                        if (recordTarget == null) {
                            recordTarget = obj;
                        }
                        AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1003);
                    }
                    if (!obj.isForgotten()) {
                        continue;
                    }
                    if (recordTarget == null) {
                        recordTarget = obj;
                    }
                    AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1006);
                }
            }
            if (recordTarget != null) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 73302);
                skillRecord.setT(recordTarget.getId());
            }
        }
    }
}
