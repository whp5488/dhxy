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
    public DRUG_10021() {
    }
    
    @Override
    public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (target == null || target.isFallDown()) {
            target = effector;
        }
        List<Integer> statusList = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap();
        if (target.getStatusValue(1002) > 0) {
            map.put(Integer.valueOf(1002), Integer.valueOf(target.getStatusValue(1002)));
            statusList.add(Integer.valueOf(1002));
            target.removeStatus(1002);
        }
        if (target.getStatusValue(1003) > 0) {
            map.put(Integer.valueOf(1003), Integer.valueOf(target.getStatusValue(1003)));
            statusList.add(Integer.valueOf(1003));
            target.removeStatus(1003);
        }
        if (target.getStatusValue(1004) > 0) {
            map.put(Integer.valueOf(1004), Integer.valueOf(target.getStatusValue(1004)));
            statusList.add(Integer.valueOf(1004));
            target.removeStatus(1004);
        }
        if (target.getStatusValue(1006) > 0) {
            map.put(Integer.valueOf(1006), Integer.valueOf(target.getStatusValue(1006)));
            statusList.add(Integer.valueOf(1006));
            target.removeStatus(1006);
        }
        ActionDetailParam actionDetailParam = ActionParamUtils.clearStatusReturn(effector, target, 5, 10021, 2, statusList);
        double percent = 0.15;
        List<FiedValue> tianFuEffects = (List)args[0];
        if (tianFuEffects != null && tianFuEffects.size() > 0) {
            for (FiedValue fied : tianFuEffects) {
                if (fied.getType().equals("10021_reducePercent") && fied.getVT() == 5) {
                    Double value = (Double)fied.getValue();
                    if (value != null && (double)value > 0.0) {
                        percent -= (double)value;
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
        }
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            int time = 0;
            int[] statuss = { 1002, 1003, 1004, 1006 };
            Random random = new Random();
            int index = random.nextInt(statuss.length);
            int status = statuss[index];
            if (map.get(Integer.valueOf(status)) != null && (int)map.get(Integer.valueOf(status)) > 0) {
                time = (int)map.get(Integer.valueOf(status));
            }
            else {
                int randomTime = random.nextInt(statusList.size());
                int randomStatus = (int)statusList.get(randomTime);
                time = (int)map.get(Integer.valueOf(randomStatus));
            }
            target.setStatus(status, time);
            FightObjectParam targetParam = ActionParamUtils.getTargetParam(actionDetailParam, (int)target.getId());
            targetParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(time));
        }
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return null;
    }
    
    @Override
    public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (effector.isASide() != target.isASide()) {
            return false;
        }
        if (target == null || target.isFallDown()) {
            target = effector;
        }
        return target.getStatusValue(1002) > 0 || target.getStatusValue(1003) > 0 || target.getStatusValue(1004) > 0 || target.getStatusValue(1006) > 0;
    }
}
