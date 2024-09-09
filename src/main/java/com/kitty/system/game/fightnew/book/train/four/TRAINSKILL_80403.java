package com.kitty.system.game.fightnew.book.train.four;

import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80403 extends TrainSkillProcessor
{
    public TRAINSKILL_80403() {
    }
    
    @Override
    public void beforeMagicAtta(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0 || fight.getRound() < 3 || objs == null || objs.length <= 0) {
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
        if (roleSkill != null || SKILLATTR.SKILLATTR_CONFUSE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_ICE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_SLEEP.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_YIWANG.getValue() == (int)roleSkill.getType()) {
            double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80403, 0);
            if (percent > 0.0) {
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), percent);
            }
        }
        return;
    }
}
