
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
    @Override
    public NutMap enteringSide(final Fight fight, final FightObject effector, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 25.0, 20.0);
        if (map != null) {
            final double percent = map.getDouble("percent");
            if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                final FightObject target = PassiveSkillUtils.getRandomTarget(fight, effector, (byte)2);
                if (target != null) {
                    final int time = 2;
                    target.setStatus(2170, time);
                    ActionParamUtils.addStatus(fight, effector, target, 7, 31516, 2, 2170, time);
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31416);
                    skillRecord.setT(target.getId());
                }
            }
        }
        return null;
    }
    
    public static void clearStatus(final Fight fight, final FightObject effector) {
        if (effector.getStatusValue(2170) > 0) {
            final int controlStatus = AwakenSkillEffectUtils.getStatus(effector, StatusConst.getControlStatus());
            if (controlStatus > 0) {
                final List<Integer> clearList = new ArrayList<Integer>();
                effector.removeStatus(2170);
                clearList.add(2170);
                effector.removeStatus(2170);
                clearList.add(controlStatus);
                ActionParamUtils.clearStatus(fight, effector, effector, 7, 300999, 2, clearList);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31516);
                skillRecord.setT(effector.getId());
            }
        }
    }
}
