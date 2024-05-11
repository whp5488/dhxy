
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_72312 extends AcsrySkillProcessor
{
    @Override
    public boolean magicSelectTarget(final Fight fight, final PlayerFightObject playerFightObject, final Object... args) {
        final RoleSkill skill = (RoleSkill)args[0];
        if (skill == null) {
            return false;
        }
        if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == skill.getType() || SKILLATTR.SKILLATTR_POJIA.getValue() == skill.getType() || SKILLATTR.SKILLATTR_ZHENJI.getValue() == skill.getType() || SKILLATTR.SKILLATTR_FIRE.getValue() == skill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == skill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == skill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == skill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == skill.getType()) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, playerFightObject.getId(), 72312);
            final double percent = 0.5;
            final boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                skillRecord.setT(playerFightObject.getId());
                return true;
            }
        }
        return false;
    }
}
