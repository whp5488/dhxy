package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80507 extends TrainSkillProcessor
{
    public TRAINSKILL_80507() {
    }
    
    @Override
    public void roundStart(Fight fight, FightObject effector, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() > 0 || fight.getRound() - effector.getComeRound() > 0) {
            return;
        }
        double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80507, 2);
        if (percent > 0.0) {
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()), WarMaxRound.WAR_MAX_ROUND.getValue(), 80507, percent);
        }
        return;
    }
    
    @Override
    public void magicAttackEnd(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0 || objs == null || objs.length <= 0) {
            return;
        }
        RoleSkill roleSkill = null;
        try {
            roleSkill = (RoleSkill)objs[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (roleSkill != null) {
            int omit = this.getOmit(roleSkill.getType());
            if (omit > 0) {
                double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80507, 0);
                if (percent != 0.0) {
                    double upperLimit = 0.8;
                    ImbueParam imbueParam = ImbueUtil.gainProForSkill(effector.getImbueMap(), Integer.valueOf(omit), 80507, true);
                    if (imbueParam.getSr() <= 0) {
                        imbueParam.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
                    }
                    double value = imbueParam.getValue() + percent;
                    if (value > upperLimit) {
                        value = upperLimit;
                    }
                    imbueParam.setValue(value);
                }
            }
        }
        return;
    }
    
    private int getOmit(Integer type) {
        int value = 0;
        if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == (int)type) {
            value = PRO_DICT_TYPE.OMIT_K_HL.getDist();
        }
        else if (SKILLATTR.SKILLATTR_ICE.getValue() == (int)type) {
            value = PRO_DICT_TYPE.OMIT_K_FY.getDist();
        }
        else if (SKILLATTR.SKILLATTR_SLEEP.getValue() == (int)type) {
            value = PRO_DICT_TYPE.OMIT_K_HS.getDist();
        }
        else if (SKILLATTR.SKILLATTR_YIWANG.getValue() == (int)type) {
            value = PRO_DICT_TYPE.OMIT_K_YW.getDist();
        }
        return value;
    }
}
