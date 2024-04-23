package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31426 extends AwakenSkillProcessor {
  public void skillIntensifyTM(Fight fight, PetFightObject petFightObject, Object... args) {
    if (args != null && args.length > 0) {
      int skillId = ((Integer)args[0]).intValue();
      List<Integer> list = (List<Integer>)args[1];
      if (80003 == skillId || 80005 == skillId || 81010 == skillId || 81011 == skillId) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 40.0D, 20.0D);
        if (map == null)
          return; 
        int num = 0;
        double percenta = map.getDouble("percent");
        if (percenta > 0.0D && SkillAi.canAffectOnPro(percenta))
          num = 1; 
        map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 20.0D, 20.0D);
        if (map == null)
          return; 
        double percentb = map.getDouble("percent");
        if (percentb > 0.0D && SkillAi.canAffectOnPro(percentb))
          num = 2; 
        if (num > 0) {
          List<FightObject> targets = PassiveSkillUtils.getTargetForCondition(fight, (FightObject)petFightObject, list, 1, true, num);
          if (targets != null && targets.size() > 0)
            for (FightObject obj : targets)
              list.add(obj.getId());  
        } 
      } 
    } 
  }
}
