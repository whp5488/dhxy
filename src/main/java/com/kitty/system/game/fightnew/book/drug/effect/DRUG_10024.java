package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;
import com.kitty.system.game.fightnew.book.drug.utils.DrugUtil;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class DRUG_10024 extends DrugProcessor {
  public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    List<FightObject> targetSide = fight.getTargetSide(effector.getId().intValue());
    for (FightObject targetObject : targetSide) {
      if (targetObject.getStatusMap().getInt(String.valueOf(1011)) > 0)
        DrugUtil.clearCloaking(fight, effector, targetObject, 10024); 
    } 
    return null;
  }
  
  public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
    List<FightObject> targetSide = fight.getTargetSide(effector.getId().intValue());
    for (FightObject targetObject : targetSide) {
      if (targetObject.getStatusMap().getInt(String.valueOf(1011)) > 0)
        return true; 
    } 
    return false;
  }
}
