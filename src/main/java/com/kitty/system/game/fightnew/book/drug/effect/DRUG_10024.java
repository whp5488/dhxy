
package com.kitty.system.game.fightnew.book.drug.effect;

import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.book.drug.utils.DrugUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;

public class DRUG_10024 extends DrugProcessor
{
    @Override
    public NutMap effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final List<FightObject> targetSide = fight.getTargetSide(effector.getId());
        for (final FightObject targetObject : targetSide) {
            if (targetObject.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                DrugUtil.clearCloaking(fight, effector, targetObject, 10024);
            }
        }
        return null;
    }
    
    @Override
    public boolean checkExcute(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final List<FightObject> targetSide = fight.getTargetSide(effector.getId());
        for (final FightObject targetObject : targetSide) {
            if (targetObject.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                return true;
            }
        }
        return false;
    }
}
