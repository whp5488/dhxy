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
    public ACSRYSKILL_72312() {
    }
    
    @Override
    public boolean magicSelectTarget(Fight fight, PlayerFightObject playerFightObject, Object... args) {
        RoleSkill skill = (RoleSkill)args[0];
        if (skill == null) {
            return false;
        }
        if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_POJIA.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_ZHENJI.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_FIRE.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == (int)skill.getType()) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)playerFightObject.getId(), 72312);
            double percent = 0.5;
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                skillRecord.setT((long)(int)playerFightObject.getId());
                return true;
            }
        }
        return false;
    }
}
