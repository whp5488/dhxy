
package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31408 extends AwakenSkillProcessor
{
    @Override
    public NutMap enteringSide(final Fight fight, final FightObject effector, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 100.0, 20.0);
        if (map != null) {
            final double percent = map.getDouble("percent");
            if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                final double value = 0.25;
                final int phyPower = effector.getPhyPower();
                final String newPhyPowerStr = BigDecimalUtil.multiply(String.valueOf(phyPower), String.valueOf(BigDecimalUtil.additionDouble(1.0, value)));
                effector.setPhyPower((int)Double.parseDouble(newPhyPowerStr));
                ActionParamUtils.actionForTargetNoth(fight, effector, effector, 7, 31408, 2);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31408);
                skillRecord.setT(effector.getId());
            }
        }
        return null;
    }
}
