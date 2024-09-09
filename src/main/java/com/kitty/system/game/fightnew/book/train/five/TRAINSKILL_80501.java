package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80501 extends TrainSkillProcessor
{
    public TRAINSKILL_80501() {
    }
    
    @Override
    public void roundStart(Fight fight, FightObject effector, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() >= 1 || fight.getRound() - effector.getComeRound() > 0) {
            return;
        }
        double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80501, 2);
        if (percent > 0.0) {
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist()), WarMaxRound.WAR_MAX_ROUND.getValue(), 80501, percent);
        }
        return;
    }
    
    @Override
    public NutMap reduceLifeEnd(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() < 1) {
            return null;
        }
        int hurt = 0;
        try {
            hurt = (int)objs[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (target.getCurrLife() <= 0 && target.isPet() && hurt < 0) {
            double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80501, 0);
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                target.getOtherMap().setv("isCoerceExit", Boolean.valueOf(true));
            }
        }
        return null;
    }
}
