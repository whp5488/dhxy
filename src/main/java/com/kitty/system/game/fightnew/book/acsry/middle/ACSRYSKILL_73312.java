
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.AcsrySkillInsideUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73312 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            final double percent = 0.1;
            FightObject recordTarget = null;
            for (final FightObject obj : fight.getMySide(fightObject.getId())) {
                if (obj.getStatusValue(1010) > 0) {
                    final int unitId = obj.getStatusSubMap().getInt("1010target");
                    if (fightObject.getId() != unitId) {
                        continue;
                    }
                    if (!SkillAi.canAffectOnPro(percent)) {
                        continue;
                    }
                    if (obj.isConfusion()) {
                        if (recordTarget == null) {
                            recordTarget = obj;
                        }
                        AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1002);
                    }
                    if (!obj.isSleep()) {
                        continue;
                    }
                    if (recordTarget == null) {
                        recordTarget = obj;
                    }
                    AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1004);
                }
            }
            if (recordTarget != null) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 73312);
                skillRecord.setT(recordTarget.getId());
            }
        }
    }
}
