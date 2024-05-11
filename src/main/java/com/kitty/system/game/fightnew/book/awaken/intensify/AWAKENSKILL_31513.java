
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31513 extends AwakenSkillProcessor
{
    @Override
    public void phyAttackEnd(final Fight fight, final FightObject effector, final Object... args) {
        if (args != null && args.length > 1) {
            final BattleUnit battleUnit = (BattleUnit)args[0];
            final ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
            if (battleUnit != null && actionDetailParam != null) {
                final FightObject target = battleUnit.getTarget();
                if (target == null || target.isFallDown()) {
                    return;
                }
                final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 25.0, 20.0);
                if (map == null) {
                    return;
                }
                final double percent = map.getDouble("percent");
                if (percent > 0.0) {
                    final int currLife = target.getCurrLife();
                    int hurt = (int)(currLife * percent);
                    if (hurt > 0) {
                        hurt = ((hurt > 300000) ? 300000 : hurt);
                        final FightObjectParam targetParam = ActionParamUtils.changeLifeForTarget(fight, target, actionDetailParam.getTargetParamList().size() + 1, -hurt, 1);
                        actionDetailParam.getTargetParamList().add(targetParam);
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31513);
                        skillRecord.setT(target.getId());
                    }
                }
            }
        }
    }
}
