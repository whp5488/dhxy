package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;
import com.kitty.system.game.fightnew.book.drug.utils.DrugUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class DRUG_10025 extends DrugProcessor {
  public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (target == null || target.isFallDown() || target.getStatusValue(1011) <= 0)
      target = fight.queryFightObject(Integer.valueOf(effector.getTargetId())); 
    DrugUtil.clearCloaking(fight, effector, target, 10025);
    return null;
  }
  
  public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (target == null || target.isFallDown() || target.getStatusValue(1011) <= 0) {
      List<FightObject> targetSide = (List<FightObject>)fight.getTargetSide(effector.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && obj.getStatusValue(1011) > 0)).collect(Collectors.toList());
      if (targetSide.size() > 0) {
        effector.setTargetId(((FightObject)targetSide.get(0)).getId().intValue());
      } else {
        return false;
      } 
    } 
    return true;
  }
}
