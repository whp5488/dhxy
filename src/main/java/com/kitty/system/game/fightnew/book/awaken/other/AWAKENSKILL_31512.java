
package com.kitty.system.game.fightnew.book.awaken.other;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31512 extends AwakenSkillProcessor
{
    @Override
    public int changeHurt(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (args == null || args.length < 1) {
            return 0;
        }
        final int index = (int)args[0];
        if (index != 1) {
            return 0;
        }
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 70.0, 20.0);
        if (map == null) {
            return 0;
        }
        final double percent = map.getDouble("percent");
        if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
            final int phyAttack = CountUtils.getPhyAttack(effector);
            final double p = 0.3;
            final double addHurt = phyAttack * p;
            if (addHurt > 0.0) {
                return (int)addHurt;
            }
        }
        return 0;
    }
}
