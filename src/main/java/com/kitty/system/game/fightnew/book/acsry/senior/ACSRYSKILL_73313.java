
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

public class ACSRYSKILL_73313 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            double percent = 0.1;
            final Role role = fightObject.getRole();
            percent += role.getMj() / 200 * 0.01;
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
                        AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1002);
                        if (recordTarget == null) {
                            recordTarget = obj;
                        }
                    }
                    if (!obj.isSleep()) {
                        continue;
                    }
                    AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1004);
                    if (recordTarget != null) {
                        continue;
                    }
                    recordTarget = obj;
                }
            }
            if (recordTarget != null) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 73313);
                skillRecord.setT(recordTarget.getId());
            }
        }
    }
}
