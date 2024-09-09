package com.kitty.system.game.fightnew.book.train.four;

import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80405 extends TrainSkillProcessor
{
    public TRAINSKILL_80405() {
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
            double percent = TrainSkillUtil.getPercent((long)target.getRole().getId(), 80405, 0);
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                List<Integer> clearStaus = new ArrayList<>();
                for (Integer status : StatusConst.getControlStatus()) {
                    if (target.getStatusValue((int)status) > 0) {
                        clearStaus.add(status);
                    }
                    else {
                        continue;
                    }
                }
                if (clearStaus.size() > 0) {
                    NutMap nutMap = NutMap.NEW();
                    nutMap.setv("type", "clearStatus");
                    nutMap.setv("clearStatus", clearStaus);
                    return nutMap;
                }
            }
        }
        return null;
    }
}
