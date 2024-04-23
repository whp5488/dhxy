package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31512 extends AwakenSkillProcessor {
  public int changeHurt(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (args == null || args.length < 1)
      return 0; 
    int index = ((Integer)args[0]).intValue();
    if (index != 1)
      return 0; 
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 70.0D, 20.0D);
    if (map == null)
      return 0; 
    double percent = map.getDouble("percent");
    if (percent > 0.0D && SkillAi.canAffectOnPro(percent)) {
      int phyAttack = CountUtils.getPhyAttack(effector);
      double p = 0.3D;
      double addHurt = phyAttack * p;
      if (addHurt > 0.0D)
        return (int)addHurt; 
    } 
    return 0;
  }
}
