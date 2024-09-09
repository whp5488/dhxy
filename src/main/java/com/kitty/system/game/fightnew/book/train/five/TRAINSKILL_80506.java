package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80506 extends TrainSkillProcessor
{
    public TRAINSKILL_80506() {
    }
    
    @Override
    public void beforeMagicAtta(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0 || objs == null || objs.length <= 0) {
            return;
        }
        RoleSkill roleSkill = null;
        try {
            roleSkill = (RoleSkill)objs[0];
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_ICE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_SLEEP.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_YIWANG.getValue() == (int)roleSkill.getType()) {
            double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80506, 0);
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                ImbueUtil.addOrUpdIgnore(effector.getImbueMap(), 1, 1, 1009, PRO_DICT_TYPE.K_HL.getDist(), 1, percent);
                ImbueUtil.addOrUpdIgnore(effector.getImbueMap(), 1, 1, 1009, PRO_DICT_TYPE.K_FY.getDist(), 1, percent);
                ImbueUtil.addOrUpdIgnore(effector.getImbueMap(), 1, 1, 1009, PRO_DICT_TYPE.K_HS.getDist(), 1, percent);
                ImbueUtil.addOrUpdIgnore(effector.getImbueMap(), 1, 1, 1009, PRO_DICT_TYPE.K_YW.getDist(), 1, percent);
            }
        }
        return;
    }
}
