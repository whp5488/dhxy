package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class DRUG_10022 extends DrugProcessor {
  public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (target == null || target.isFallDown())
      target = effector; 
    List<Integer> statusList = new ArrayList<>();
    if (target.getStatusValue(1002) > 0) {
      statusList.add(Integer.valueOf(1002));
      target.removeStatus(1002);
    } 
    if (target.getStatusValue(1003) > 0) {
      statusList.add(Integer.valueOf(1003));
      target.removeStatus(1003);
    } 
    if (target.getStatusValue(1004) > 0) {
      statusList.add(Integer.valueOf(1004));
      target.removeStatus(1004);
    } 
    if (target.getStatusValue(1006) > 0) {
      statusList.add(Integer.valueOf(1006));
      target.removeStatus(1006);
    } 
    ActionParamUtils.clearStatus(fight, effector, target, 5, 10022, 2, statusList);
    return null;
  }
  
  public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
    double percent = 0.5D;
    List<FiedValue> tianFuEffects = (List<FiedValue>)args[0];
    if (tianFuEffects != null && tianFuEffects.size() > 0)
      for (FiedValue fied : tianFuEffects) {
        if (fied.getType().equals("10022_addPercent") && fied.getVT() == 5) {
          Double value = (Double)fied.getValue();
          if (value != null && value.doubleValue() > 0.0D)
            percent += value.doubleValue(); 
        } 
      }  
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      if (target == null || target.isFallDown())
        target = effector; 
      if (target.getStatusValue(1002) <= 0 && target.getStatusValue(1003) <= 0 && target
        .getStatusValue(1004) <= 0 && target.getStatusValue(1006) <= 0)
        return false; 
      return true;
    } 
    return false;
  }
}
