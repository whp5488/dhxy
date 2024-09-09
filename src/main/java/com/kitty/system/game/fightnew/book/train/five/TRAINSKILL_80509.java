package com.kitty.system.game.fightnew.book.train.five;

import java.util.List;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.Iterator;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80509 extends TrainSkillProcessor
{
    public TRAINSKILL_80509() {
    }
    
    @Override
    public void magicAttackEnd(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() < 1 || objs == null || objs.length < 2) {
            return;
        }
        ActionDetailParam actionDetailParam = null;
        try {
            actionDetailParam = (ActionDetailParam)objs[1];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (actionDetailParam != null && this.isCheck(actionDetailParam)) {
            FightObject petObject = null;
            int pos = effector.getPos() + 100;
            for (FightObject obj : fight.getMySide((int)effector.getId())) {
                if (obj.getPos() == pos) {
                    petObject = obj;
                    break;
                }
                continue;
            }
            if (petObject == null) {
                double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80509, 0);
                boolean flag = SkillAi.canAffectOnPro(percent);
                if (flag) {
                    effector.getOtherMap().setv("isTriggerSupportPet", Boolean.valueOf(true));
                }
            }
        }
        return;
    }
    
    private boolean isCheck(ActionDetailParam actionDetailParam) {
        List<FightObjectParam> targetParamList = actionDetailParam.getTargetParamList();
        boolean flag = false;
        if (targetParamList != null && targetParamList.size() > 0) {
            for (FightObjectParam fightObjectParam : targetParamList) {
                if (fightObjectParam.isHit()) {
                    flag = true;
                    break;
                }
                continue;
            }
        }
        return flag;
    }
}
