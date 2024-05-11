
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75103 extends AcsrySkillProcessor
{
    @Override
    public NutMap afterPrintCommand(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown() && fightObject.getDieRound() <= fight.getRound() - 1) {
            final double percent = 0.1;
            final boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                final double addHpPercent = 0.3;
                final int addHp = (int)(fightObject.getMaxLife() * addHpPercent);
                ActionParamUtils.changeLife(fight, fightObject, fightObject, 7, 300999, 2, addHp, true);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 75103);
                skillRecord.setT(fightObject.getId());
            }
        }
        return null;
    }
}
