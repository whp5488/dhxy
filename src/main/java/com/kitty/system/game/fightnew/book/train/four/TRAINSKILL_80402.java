package com.kitty.system.game.fightnew.book.train.four;

import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80402 extends TrainSkillProcessor
{
    public TRAINSKILL_80402() {
    }
    
    @Override
    public int changeHurt(Fight fight, FightObject effector, FightObject target, Object... objs) {
        if (effector.isPlayer() && fight.isPvp() == 1 && objs != null && objs.length > 0) {
            int hurt = 0;
            try {
                hurt = (int)objs[0];
            }
            catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            if (hurt > 0) {
                int effectorDict = this.getMaxWuxin(effector);
                int targetDict = this.getMaxWuxin(target);
                if (effectorDict != 0 && targetDict != 0 && effectorDict == targetDict) {
                    double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80402, 0);
                    return (int)((double)hurt * percent);
                }
            }
        }
        return 0;
    }
    
    private int getMaxWuxin(FightObject object) {
        List<Integer> wuXinDists = PRO_DICT_TYPE.getWuXinDist();
        int value = -9999;
        int dict = 0;
        for (Integer wuXin : wuXinDists) {
            double proValue = ImbueUtil.getProValue(object.getImbueMap(), wuXin);
            if (proValue != 0.0 && (double)value < proValue) {
                value = (int)proValue;
                dict = (int)wuXin;
            }
            else {
                continue;
            }
        }
        return dict;
    }
}
