package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80508 extends TrainSkillProcessor
{
    public TRAINSKILL_80508() {
    }
    
    @Override
    public void magicAttackEnd(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0 || objs == null || objs.length < 2) {
            return;
        }
        ActionDetailParam actionDetailParam = null;
        try {
            actionDetailParam = (ActionDetailParam)objs[1];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (actionDetailParam != null) {
            double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80508, 0);
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                int round = 1;
                int status = 1011;
                effector.setStatus(status, round);
                FightObjectParam effectorParam = actionDetailParam.gainTargetParam((int)effector.getId(), true);
                effectorParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(round));
            }
        }
        return;
    }
}
