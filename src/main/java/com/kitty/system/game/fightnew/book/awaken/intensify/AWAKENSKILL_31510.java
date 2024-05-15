
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31510 extends AwakenSkillProcessor
{
    @Override
    public NutMap skillIntensify(final Fight fight, final PetFightObject petFightObject, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 50.0, 20.0);
        if (map != null && fight.getRound() >= 6) {
            double percent = map.getDouble("percent");
            percent *= 0.1;
            map.setv("percent", (Object)percent);
        }
        return map;
    }
    
    @Override
    public void beforeDoAction(final Fight fight, final FightObject effector, final Object... args) {
        if (effector.isPet()) {
            final int status = this.getStatus(effector);
            if (status <= 0) {
                return;
            }
            final NutMap map = this.skillIntensify(fight, (PetFightObject)effector);
            if (map == null) {
                return;
            }
            final double percent = map.getDouble("percent");
            if (percent >= 0.0 && SkillAi.canAffectOnPro(percent)) {
                effector.removeStatus(status);
                final List<Integer> clearList = new ArrayList<Integer>();
                clearList.add(status);
                ActionParamUtils.clearStatus(fight, effector, effector, 7, 300999, 2, clearList);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31510);
                skillRecord.setT(effector.getId());
            }
        }
    }
    
    private int getStatus(final FightObject effector) {
        if (effector.getStatusValue(1003) > 0) {
            return 1003;
        }
        if (effector.getStatusValue(1002) > 0) {
            return 1002;
        }
        if (effector.getStatusValue(1004) > 0) {
            return 1004;
        }
        if (effector.getStatusValue(1006) > 0) {
            return 1006;
        }
        return 0;
    }
}
