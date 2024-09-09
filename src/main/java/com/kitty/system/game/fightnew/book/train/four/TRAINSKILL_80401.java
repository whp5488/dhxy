package com.kitty.system.game.fightnew.book.train.four;

import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.Arrays;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80401 extends TrainSkillProcessor
{
    public TRAINSKILL_80401() {
    }
    
    @Override
    public void roundStart(Fight fight, FightObject effector, Object... objs) {
        if (!effector.isPlayer() || fight.isPvp() <= 0) {
            return;
        }
        double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80401, 0);
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            List<Integer> statusList = Arrays.asList(Integer.valueOf(1008), Integer.valueOf(1009), Integer.valueOf(1010), Integer.valueOf(1022));
            for (FightObject object : fight.getMySide((int)effector.getId())) {
                if (object.getId().equals(effector.getId())) {
                    continue;
                }
                for (Integer status : statusList) {
                    if (effector.getStatusValue((int)status) <= 0 && object.getStatusValue((int)status) > 0) {
                        if (1008 == (int)status) {
                            effector.setStatus((int)status, object.getStatusValue((int)status));
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.HIT_RATIO.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.RISE_ATTACK.getDist());
                            ActionParamUtils.addStatus(fight, effector, effector, 43, 80401, 2, (int)status, object.getStatusValue((int)status));
                        }
                        else if (1009 == (int)status) {
                            effector.setStatus((int)status, object.getStatusValue((int)status));
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_FY.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_HS.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_HL.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_YW.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_F.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_L.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_S.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_H.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_SS.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_GH.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.K_DU.getDist());
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.WL_XS.getDist());
                            ActionParamUtils.addStatus(fight, effector, effector, 43, 80401, 2, (int)status, object.getStatusValue((int)status));
                        }
                        else if (1010 == (int)status) {
                            effector.setStatus((int)status, object.getStatusValue((int)status));
                            this.updImbue(effector, object, status, PRO_DICT_TYPE.RISE_SPEED.getDist());
                            ActionParamUtils.addStatus(fight, effector, effector, 43, 80401, 2, (int)status, object.getStatusValue((int)status));
                        }
                        if (1022 == (int)status) {
                            effector.setStatus((int)status, object.getStatusValue((int)status));
                            effector.getStatusSubMap().setv(String.valueOf(1022) + "value", Integer.valueOf(object.getStatusSubMap().getInt(String.valueOf(1022) + "value", 0)));
                            ActionParamUtils.addStatus(fight, effector, effector, 43, 80401, 2, (int)status, object.getStatusValue((int)status));
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        return;
    }
    
    private void updImbue(FightObject effector, FightObject target, Integer status, int dict) {
        ImbueParam statusValue = ImbueUtil.getStatusValue(target.getImbueMap(), (int)status, dict);
        if (statusValue != null && statusValue.getValue() > 0.0) {
            ImbueUtil.addOrUpdStatus(effector.getImbueMap(), (int)status, dict, statusValue.getSr(), statusValue.getValue());
        }
    }
}
