package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80504 extends TrainSkillProcessor
{
    public TRAINSKILL_80504() {
    }
    
    @Override
    public int changeHurtMp(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (target.isPlayer() && fight.isPvp() == 1 && objs != null && objs.length > 1) {
            RoleSkill roleSkill = null;
            int hurtMp = 0;
            try {
                hurtMp = (int)objs[0];
                roleSkill = (RoleSkill)objs[1];
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (hurtMp != 0 && roleSkill != null && this.isCheck(roleSkill)) {
                double percent = TrainSkillUtil.getPercent((long)target.getRole().getId(), 80504, 0);
                int changeHurt = (int)((double)hurtMp * percent);
                if (changeHurt != 0) {
                    return -changeHurt;
                }
            }
        }
        return 0;
    }
    
    private boolean isCheck(RoleSkill roleSkill) {
        return PETSKILLATTR.PETSKILLATTR_FG.getValue() == (int)roleSkill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == (int)roleSkill.getType();
    }
}
