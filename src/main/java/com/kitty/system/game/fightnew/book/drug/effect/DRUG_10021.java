
package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.Iterator;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import java.util.Map;
import java.util.Random;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.HashMap;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;

public class DRUG_10021 extends DrugProcessor
{
    @Override
    public NutMap effect(final Fight fight, final FightObject effector, FightObject target, final Object... args) {
        if (target == null || target.isFallDown()) {
            target = effector;
        }
        final List<Integer> statusList = new ArrayList<Integer>();
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        if (target.getStatusValue(1002) > 0) {
            map.put(1002, target.getStatusValue(1002));
            statusList.add(1002);
            target.removeStatus(1002);
        }
        if (target.getStatusValue(1003) > 0) {
            map.put(1003, target.getStatusValue(1003));
            statusList.add(1003);
            target.removeStatus(1003);
        }
        if (target.getStatusValue(1004) > 0) {
            map.put(1004, target.getStatusValue(1004));
            statusList.add(1004);
            target.removeStatus(1004);
        }
        if (target.getStatusValue(1006) > 0) {
            map.put(1006, target.getStatusValue(1006));
            statusList.add(1006);
            target.removeStatus(1006);
        }
        final ActionDetailParam actionDetailParam = ActionParamUtils.clearStatusReturn(effector, target, 5, 10021, 2, statusList);
        double percent = 0.15;
        final List<FiedValue> tianFuEffects = (List<FiedValue>)args[0];
        if (tianFuEffects != null && tianFuEffects.size() > 0) {
            for (final FiedValue fied : tianFuEffects) {
                if (fied.getType().equals("10021_reducePercent") && fied.getVT() == 5) {
                    final Double value = (Double)fied.getValue();
                    if (value == null || value <= 0.0) {
                        continue;
                    }
                    percent -= value;
                }
            }
        }
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            int time = 0;
            final int[] statuss = { 1002, 1003, 1004, 1006 };
            final Random random = new Random();
            final int index = random.nextInt(statuss.length);
            final int status = statuss[index];
            if (map.get(status) != null && map.get(status) > 0) {
                time = map.get(status);
            }
            else {
                final int randomTime = random.nextInt(statusList.size());
                final int randomStatus = statusList.get(randomTime);
                time = map.get(randomStatus);
            }
            target.setStatus(status, time);
            final FightObjectParam targetParam = ActionParamUtils.getTargetParam(actionDetailParam, target.getId());
            targetParam.getStatusMap().put(status, time);
        }
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return null;
    }
    
    @Override
    public boolean checkExcute(final Fight fight, final FightObject effector, FightObject target, final Object... args) {
        if (effector.isASide() != target.isASide()) {
            return false;
        }
        if (target == null || target.isFallDown()) {
            target = effector;
        }
        return target.getStatusValue(1002) > 0 || target.getStatusValue(1003) > 0 || target.getStatusValue(1004) > 0 || target.getStatusValue(1006) > 0;
    }
}
