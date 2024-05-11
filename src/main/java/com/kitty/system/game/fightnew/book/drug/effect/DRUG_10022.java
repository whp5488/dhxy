
package com.kitty.system.game.fightnew.book.drug.effect;

import java.util.Iterator;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;

public class DRUG_10022 extends DrugProcessor
{
    @Override
    public NutMap effect(final Fight fight, final FightObject effector, FightObject target, final Object... args) {
        if (target == null || target.isFallDown()) {
            target = effector;
        }
        final List<Integer> statusList = new ArrayList<Integer>();
        if (target.getStatusValue(1002) > 0) {
            statusList.add(1002);
            target.removeStatus(1002);
        }
        if (target.getStatusValue(1003) > 0) {
            statusList.add(1003);
            target.removeStatus(1003);
        }
        if (target.getStatusValue(1004) > 0) {
            statusList.add(1004);
            target.removeStatus(1004);
        }
        if (target.getStatusValue(1006) > 0) {
            statusList.add(1006);
            target.removeStatus(1006);
        }
        ActionParamUtils.clearStatus(fight, effector, target, 5, 10022, 2, statusList);
        return null;
    }
    
    @Override
    public boolean checkExcute(final Fight fight, final FightObject effector, FightObject target, final Object... args) {
        double percent = 0.5;
        final List<FiedValue> tianFuEffects = (List<FiedValue>)args[0];
        if (tianFuEffects != null && tianFuEffects.size() > 0) {
            for (final FiedValue fied : tianFuEffects) {
                if (fied.getType().equals("10022_addPercent") && fied.getVT() == 5) {
                    final Double value = (Double)fied.getValue();
                    if (value == null || value <= 0.0) {
                        continue;
                    }
                    percent += value;
                }
            }
        }
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            if (target == null || target.isFallDown()) {
                target = effector;
            }
            return target.getStatusValue(1002) > 0 || target.getStatusValue(1003) > 0 || target.getStatusValue(1004) > 0 || target.getStatusValue(1006) > 0;
        }
        return false;
    }
}
