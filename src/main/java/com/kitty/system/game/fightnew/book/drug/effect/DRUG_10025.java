
package com.kitty.system.game.fightnew.book.drug.effect;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.book.drug.utils.DrugUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;

public class DRUG_10025 extends DrugProcessor
{
    @Override
    public NutMap effect(final Fight fight, final FightObject effector, FightObject target, final Object... args) {
        if (target == null || target.isFallDown() || target.getStatusValue(1011) <= 0) {
            target = fight.queryFightObject(effector.getTargetId());
        }
        DrugUtil.clearCloaking(fight, effector, target, 10025);
        return null;
    }
    
    @Override
    public boolean checkExcute(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (target == null || target.isFallDown() || target.getStatusValue(1011) <= 0) {
            final List<FightObject> targetSide = fight.getTargetSide(effector.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getStatusValue(1011) > 0).collect(Collectors.toList());
            if (targetSide.size() <= 0) {
                return false;
            }
            effector.setTargetId(targetSide.get(0).getId());
        }
        return true;
    }
}
