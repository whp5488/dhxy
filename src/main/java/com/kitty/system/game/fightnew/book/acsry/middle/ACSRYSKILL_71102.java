
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71102 extends AcsrySkillProcessor
{
    @Override
    public void beforeExecuteMagic(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList.contains(71102)) {
                final RoleSkill skill = (RoleSkill)args[0];
                if (skill == null) {
                    return;
                }
                final int property = this.getProperty(skill);
                if (property == 0) {
                    return;
                }
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 71102);
                final double percent = 0.6;
                final boolean flag = SkillAi.canAffectOnPro(percent);
                if (flag) {
                    final String dictStr = String.valueOf(property);
                    playerFightObject.getAcsryPropertyMap().setv(dictStr, (Object)(-1));
                    playerFightObject.getAcsryPropertyMap().setv(dictStr + "value", 0.03);
                    skillRecord.setT(target.getId());
                }
            }
        }
    }
    
    private int getProperty(final RoleSkill skill) {
        if (SKILLATTR.SKILLATTR_ICE.getValue() == skill.getType()) {
            return PRO_DICT_TYPE.OMIT_K_FY.getDist();
        }
        if (SKILLATTR.SKILLATTR_SLEEP.getValue() == skill.getType()) {
            return PRO_DICT_TYPE.OMIT_K_HS.getDist();
        }
        return 0;
    }
}
