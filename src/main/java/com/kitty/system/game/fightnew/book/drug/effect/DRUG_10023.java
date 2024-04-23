package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import org.nutz.lang.util.NutMap;

public class DRUG_10023 extends DrugProcessor {
  public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (target == null || target.isFallDown())
      target = effector; 
    int time = 3;
    target.setStatus(1011, time);
    effector.getSkillCoolMap().setv(String.valueOf(10023), Integer.valueOf(fight.getMaxRound()));
    ActionParamUtils.addStatus(fight, effector, target, 5, 10023, 2, 1011, time);
    return null;
  }
  
  public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (effector.getSkillCoolMap().getInt(String.valueOf(10023)) > 0)
      return false; 
    return true;
  }
}
