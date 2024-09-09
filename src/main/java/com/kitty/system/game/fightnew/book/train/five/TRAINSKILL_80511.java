package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80511 extends TrainSkillProcessor
{
    public TRAINSKILL_80511() {
    }
    
    @Override
    public void roundStart(Fight fight, FightObject effector, Object... objs) {
        if (!effector.isPlayer() || fight.getRound() - effector.getComeRound() > 0) {
            return;
        }
        if (fight.isPvp() == 0) {
            double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80511, 2);
            if (percent > 0.0) {
                ImbueUtil.addOrUpdSkill(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), WarMaxRound.WAR_MAX_ROUND.getValue(), 80511, percent);
            }
        }
        else if (fight.isPvp() == 1) {
            double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80511, 0);
            if (percent > 0.0) {
                ImbueUtil.addOrUpdSkill(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), WarMaxRound.WAR_MAX_ROUND.getValue(), 80511, percent);
            }
        }
        return;
    }
    
    @Override
    public NutMap reduceLifeEnd(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!target.isPlayer() || fight.isPvp() <= 0 || objs == null || objs.length <= 0) {
            return null;
        }
        int hurt = 0;
        try {
            hurt = (int)objs[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (hurt < 0) {
            this.excuteMethod(target);
        }
        return null;
    }
    
    private void excuteMethod(FightObject target) {
        ImbueParam imbueParam = ImbueUtil.gainProForSkill(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), 80511, false);
        if (imbueParam.getValue() > 0.0) {
            double percent = TrainSkillUtil.getPercent((long)target.getRole().getId(), 80511, 0);
            double reducePercent = TrainSkillUtil.getPercent((long)target.getRole().getId(), 80511, 1);
            double reduce = percent * reducePercent;
            if (imbueParam.getValue() < reduce) {
                reduce = imbueParam.getValue();
            }
            if (reduce > 0.0) {
                imbueParam.setValue(imbueParam.getValue() - reduce);
            }
        }
    }
}
