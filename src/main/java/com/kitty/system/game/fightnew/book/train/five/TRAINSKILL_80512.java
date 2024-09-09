package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80512 extends TrainSkillProcessor
{
    public TRAINSKILL_80512() {
    }
    
    @Override
    public void magicAttackEndOne(Fight fight, FightObject effector, FightObject target, Object... objs) {
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
        List<FightObject> mySide = fight.getMySide((int)effector.getId());
        boolean flag = true;
        for (FightObject obj : mySide) {
            if (obj.getId().equals(effector.getId())) {
                continue;
            }
            if (obj.getSkillCoolMap().getInt(String.valueOf(80512)) > 0) {
                flag = false;
                break;
            }
            continue;
        }
        if (roleSkill != null && flag) {
            int omit = this.getOmit(roleSkill.getType());
            if (omit > 0) {
                ImbueUtil.addOrUpdSkill(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), 5, 80512, 0.65);
                double selfAllTempPro = target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                if (selfAllTempPro > 0.0) {
                    double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80512, 0);
                    if (percent > 0.0) {
                        ImbueUtil.reducePro(target, PRO_DICT_TYPE.SH_DK.getDist(), percent);
                        ImbueParam imbueParam = ImbueUtil.gainProForSkill(effector.getImbueMap(), Integer.valueOf(omit), 80512, true);
                        double roundOmitValue = imbueParam.getRecordMap().getDouble(fight.getRound() + "_omitValue", 0.0);
                        double percent2 = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80512, 1);
                        double upperLimitRound = 0.2;
                        double upperLimit = 0.8;
                        double v = upperLimit - imbueParam.getValue();
                        double rv = upperLimitRound - roundOmitValue;
                        if (v > 0.0 && rv > 0.0 && percent2 > 0.0) {
                            double value = this.getValue(Arrays.asList(Double.valueOf(percent2), Double.valueOf(v), Double.valueOf(rv)));
                            if (value > 0.0) {
                                imbueParam.getRecordMap().setv(fight.getRound() + "_omitValue", Double.valueOf(roundOmitValue + value));
                                imbueParam.setValue(imbueParam.getValue() + value);
                            }
                            effector.getSkillCoolMap().setv(String.valueOf(80512), Integer.valueOf(1));
                        }
                    }
                }
            }
        }
        return;
    }
    
    private double getValue(List<Double> list) {
        double value = 999.0;
        for (Double d : list) {
            if (value > (double)d) {
                value = (double)d;
            }
            else {
                continue;
            }
        }
        return value;
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
