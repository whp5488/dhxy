package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80510 extends TrainSkillProcessor
{
    public TRAINSKILL_80510() {
    }
    
    @Override
    public void normalAttackAfter(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0 || objs == null || objs.length < 1) {
            return;
        }
        BattleUnit battleUnit = null;
        try {
            battleUnit = (BattleUnit)objs[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (battleUnit != null && battleUnit.isDodge() && (effector.getNormalAttackObjs() == null || !effector.getNormalAttackObjs().contains(battleUnit.getTarget().getId()))) {
            effector.addNormalAttackObjs(target.getId());
            double upperLimit = 0.8;
            ImbueParam imbueParam = ImbueUtil.gainProForSkill(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.ATTACK_ADDITION.getDist()), 80510, true);
            if (imbueParam.getSr() <= 0) {
                imbueParam.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
            }
            double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80510, 0);
            double value = imbueParam.getValue() + percent;
            if (value > upperLimit) {
                value = upperLimit;
            }
            imbueParam.setValue(value);
        }
        return;
    }
}
