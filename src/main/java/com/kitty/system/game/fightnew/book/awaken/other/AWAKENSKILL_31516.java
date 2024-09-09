package com.kitty.system.game.fightnew.book.awaken.other;

import java.util.List;
import java.util.ArrayList;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31516 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31516() {
    }
    
    @Override
    public NutMap enteringSide(Fight fight, FightObject effector, Object... args) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 25.0, 20.0);
        if (map != null) {
            double percent = map.getDouble("percent");
            if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                FightObject target = PassiveSkillUtils.getRandomTarget(fight, effector, (byte)2);
                if (target != null) {
                    int time = 2;
                    target.setStatus(2170, time);
                    ActionParamUtils.addStatus(fight, effector, target, 7, 31516, 2, 2170, time);
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31416);
                    skillRecord.setT((long)(int)target.getId());
                }
            }
        }
        return null;
    }
    
    public static void clearStatus(Fight fight, FightObject effector) {
        if (effector.getStatusValue(2170) > 0) {
            int controlStatus = AwakenSkillEffectUtils.getStatus(effector, StatusConst.getControlStatus());
            if (controlStatus > 0) {
                List<Integer> clearList = new ArrayList<>();
                effector.removeStatus(2170);
                clearList.add(Integer.valueOf(2170));
                effector.removeStatus(2170);
                clearList.add(Integer.valueOf(controlStatus));
                ActionParamUtils.clearStatus(fight, effector, effector, 7, 300999, 2, clearList);
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31516);
                skillRecord.setT((long)(int)effector.getId());
            }
        }
    }
}
