package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.nutz.lang.util.NutMap;

public class DRUG_10021 extends DrugProcessor {
  public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (target == null || target.isFallDown())
      target = effector; 
    List<Integer> statusList = new ArrayList<>();
    Map<Integer, Integer> map = new HashMap<>();
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
    double percent = 0.15D;
    List<FiedValue> tianFuEffects = (List<FiedValue>)args[0];
    if (tianFuEffects != null && tianFuEffects.size() > 0)
      for (FiedValue fied : tianFuEffects) {
        if (fied.getType().equals("10021_reducePercent") && fied.getVT() == 5) {
          Double value = (Double)fied.getValue();
          if (value != null && value.doubleValue() > 0.0D)
            percent -= value.doubleValue(); 
        } 
      }  
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      int time = 0;
      int[] statuss = { 1002, 1003, 1004, 1006 };
      Random random = new Random();
      int index = random.nextInt(statuss.length);
      int status = statuss[index];
      if (map.get(Integer.valueOf(status)) != null && ((Integer)map.get(Integer.valueOf(status))).intValue() > 0) {
        time = ((Integer)map.get(Integer.valueOf(status))).intValue();
      } else {
        int randomTime = random.nextInt(statusList.size());
        int randomStatus = ((Integer)statusList.get(randomTime)).intValue();
        time = ((Integer)map.get(Integer.valueOf(randomStatus))).intValue();
      } 
      target.setStatus(status, time);
      FightObjectParam targetParam = ActionParamUtils.getTargetParam(actionDetailParam, target.getId().intValue());
      targetParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(time));
    } 
    fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    return null;
  }
  
  public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (effector.isASide() != target.isASide())
      return false; 
    if (target == null || target.isFallDown())
      target = effector; 
    if (target.getStatusValue(1002) <= 0 && target.getStatusValue(1003) <= 0 && target
      .getStatusValue(1004) <= 0 && target.getStatusValue(1006) <= 0)
      return false; 
    return true;
  }
}
