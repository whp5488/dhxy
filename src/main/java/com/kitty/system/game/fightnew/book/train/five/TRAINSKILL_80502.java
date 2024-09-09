package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80502 extends TrainSkillProcessor
{
    public TRAINSKILL_80502() {
    }
    
    @Override
    public int changeHurt(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (effector.isPlayer() && fight.isPvp() == 1 && objs != null && objs.length > 1) {
            RoleSkill roleSkill = null;
            try {
                roleSkill = (RoleSkill)objs[1];
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (roleSkill != null && this.isCheck(roleSkill) && target.getCurrLife() > effector.getCurrLife()) {
                double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80502, 0);
                int changeHurt = (int)((double)(target.getCurrLife() - effector.getCurrLife()) * percent);
                if (changeHurt != 0) {
                    return changeHurt;
                }
            }
        }
        return 0;
    }
    
    private boolean isCheck(RoleSkill roleSkill) {
        return SKILLATTR.SKILLATTR_FIRE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_HENGSAO.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_POJIA.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_ZHENJI.getValue() == (int)roleSkill.getType();
    }
}
