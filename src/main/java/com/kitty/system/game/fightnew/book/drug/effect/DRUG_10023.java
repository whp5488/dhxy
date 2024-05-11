
package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;

public class DRUG_10023 extends DrugProcessor
{
    @Override
    public NutMap effect(final Fight fight, final FightObject effector, FightObject target, final Object... args) {
        if (target == null || target.isFallDown()) {
            target = effector;
        }
        final int time = 3;
        target.setStatus(1011, time);
        effector.getSkillCoolMap().setv(String.valueOf(10023), (Object)fight.getMaxRound());
        ActionParamUtils.addStatus(fight, effector, target, 5, 10023, 2, 1011, time);
        return null;
    }
    
    @Override
    public boolean checkExcute(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return effector.getSkillCoolMap().getInt(String.valueOf(10023)) <= 0;
    }
}
