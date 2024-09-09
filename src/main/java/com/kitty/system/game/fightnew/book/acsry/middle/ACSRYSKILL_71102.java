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
    public ACSRYSKILL_71102() {
    }
    
    @Override
    public void beforeExecuteMagic(Fight fight, FightObject effecter, FightObject target, Object... args) {
        if (effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList.contains(Integer.valueOf(71102))) {
                RoleSkill skill = (RoleSkill)args[0];
                if (skill == null) {
                    return;
                }
                int property = this.getProperty(skill);
                if (property == 0) {
                    return;
                }
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 71102);
                double percent = 0.6;
                boolean flag = SkillAi.canAffectOnPro(percent);
                if (flag) {
                    String dictStr = String.valueOf(property);
                    playerFightObject.getAcsryPropertyMap().setv(dictStr, Integer.valueOf(-1));
                    playerFightObject.getAcsryPropertyMap().setv(dictStr + "value", Double.valueOf(0.03));
                    skillRecord.setT((long)(int)target.getId());
                }
            }
        }
    }
    
    private int getProperty(RoleSkill skill) {
        if (SKILLATTR.SKILLATTR_ICE.getValue() == (int)skill.getType()) {
            return PRO_DICT_TYPE.OMIT_K_FY.getDist();
        }
        if (SKILLATTR.SKILLATTR_SLEEP.getValue() == (int)skill.getType()) {
            return PRO_DICT_TYPE.OMIT_K_HS.getDist();
        }
        return 0;
    }
}
