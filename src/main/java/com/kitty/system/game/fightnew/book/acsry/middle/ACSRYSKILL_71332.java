
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71332 extends AcsrySkillProcessor
{
    @Override
    public void beforeExecuteMagic(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList.contains(71332)) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 71332);
                final double percent = 0.5;
                final boolean flag = SkillAi.canAffectOnPro(percent);
                if (flag) {
                    final String dictStr = String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist());
                    playerFightObject.getAcsryPropertyMap().setv(dictStr, (Object)(-1));
                    final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_DU.getDist());
                    playerFightObject.getAcsryPropertyMap().setv(dictStr + "value", (Object)kx);
                    skillRecord.setT(target.getId());
                }
            }
        }
    }
}
