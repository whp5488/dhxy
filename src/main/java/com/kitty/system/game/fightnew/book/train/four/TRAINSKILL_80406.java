package com.kitty.system.game.fightnew.book.train.four;

import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.vo.utils.StatusConst;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80406 extends TrainSkillProcessor
{
    public TRAINSKILL_80406() {
    }
    
    @Override
    public void roundStart(Fight fight, FightObject effector, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0 || fight.getRound() - effector.getComeRound() > 0) {
            return;
        }
        double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80406, 0);
        if (percent > 0.0) {
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), WarMaxRound.WAR_MAX_ROUND.getValue(), 80406, percent);
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
    
    @Override
    public NutMap addStatus(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0 || objs == null || objs.length <= 0) {
            return null;
        }
        Integer status = Integer.valueOf(0);
        try {
            status = Integer.valueOf((int)objs[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (StatusConst.getControlStatus().contains(status)) {
            this.excuteMethod(target);
        }
        return null;
    }
    
    private void excuteMethod(FightObject target) {
        ImbueParam imbueParam = ImbueUtil.gainProForSkill(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), 80406, false);
        if (imbueParam.getValue() > 0.0) {
            double percent = TrainSkillUtil.getPercent((long)target.getRole().getId(), 80406, 0);
            double reducePercent = TrainSkillUtil.getPercent((long)target.getRole().getId(), 80406, 1);
            String divide = BigDecimalUtil.multiply(String.valueOf(percent), String.valueOf(reducePercent));
            double reduce = (double)Double.valueOf(divide);
            if (imbueParam.getValue() < reduce) {
                reduce = imbueParam.getValue();
            }
            if (reduce > 0.0) {
                String value = BigDecimalUtil.subtract(String.valueOf(imbueParam.getValue()), String.valueOf(reduce));
                imbueParam.setValue(Double.parseDouble(value));
            }
        }
    }
}
