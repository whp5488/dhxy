
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import java.util.Arrays;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31504 extends AwakenSkillProcessor
{
    @Override
    public void phyAttackEnd(final Fight fight, final FightObject effector, final Object... args) {
        if (args != null && args.length > 1) {
            final BattleUnit battleUnit = (BattleUnit)args[0];
            final ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
            if (battleUnit != null && actionDetailParam != null) {
                final FightObject target = battleUnit.getTarget();
                if (target == null || !target.isFallDown()) {
                    return;
                }
                final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 100.0, 20.0);
                if (map == null) {
                    return;
                }
                final double percent = map.getDouble("percent");
                if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                    final FightObject unit = SkillAi.effectProBass(fight, effector, 1, Arrays.asList(new String[0]));
                    final int addLife = (int)(unit.getMaxLife() * 0.6);
                    if (addLife > 0) {
                        final FightObjectParam unitParam = actionDetailParam.gainTargetParam(unit.getId(), true);
                        PassiveSkillUtils.excuteChangeLife(fight, unit, unitParam, addLife);
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31504);
                        skillRecord.setT(unit.getId());
                    }
                }
            }
        }
    }
}
