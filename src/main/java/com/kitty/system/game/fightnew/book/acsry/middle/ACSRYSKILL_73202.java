
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
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (fightObject.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList.contains(73203)) {
                return;
            }
            FightObject recordTarget = null;
            for (final FightObject obj : fight.getMySide(fightObject.getId())) {
                if (obj.getStatusValue(1009) > 0) {
                    final int unitId = obj.getStatusSubMap().getInt("1009target");
                    if (fightObject.getId() != unitId) {
                        continue;
                    }
                    if (recordTarget == null) {
                        recordTarget = obj;
                    }
                    final int oldLife = obj.getCurrLife();
                    int addLife = (int)(obj.getMaxLife() * 0.1);
                    if (addLife + oldLife > obj.getMaxLife()) {
                        addLife = obj.getMaxLife() - obj.getCurrLife();
                    }
                    if (addLife <= 0) {
                        continue;
                    }
                    ActionParamUtils.changeLife(fight, fightObject, obj, 7, 300999, 2, addLife, false);
                }
            }
            if (recordTarget != null) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 73202);
                skillRecord.setT(recordTarget.getId());
            }
        }
    }
}
