package com.kitty.system.game.fightnew.book.train.four;

import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80404 extends TrainSkillProcessor
{
    public TRAINSKILL_80404() {
    }
    
    @Override
    public NutMap addStatus(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0 || objs == null || objs.length <= 0) {
            return null;
        }
        int status = 0;
        try {
            status = (int)objs[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (status == 1011) {
            double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80404, 0);
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                NutMap nutMap = NutMap.NEW();
                nutMap.setv("objId", effector.getId());
                nutMap.setv("status", Integer.valueOf(1011));
                nutMap.setv("round", Integer.valueOf(target.getStatusValue(1011)));
                return nutMap;
            }
        }
        return null;
    }
}
