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

public class AWAKENSKILL_31407 extends AwakenSkillProcessor {
  public void skillIntensifyTM(Fight fight, PetFightObject petFightObject, Object... args) {
    if (args != null && args.length > 0) {
      int skillId = ((Integer)args[0]).intValue();
      List<Integer> list = (List<Integer>)args[1];
      if (80007 == skillId || 80009 == skillId || 81012 == skillId || 81013 == skillId) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 50.0D, 20.0D);
        if (map == null)
          return; 
        double percent = map.getDouble("percent");
        if (percent > 0.0D && SkillAi.canAffectOnPro(percent)) {
          List<FightObject> targets = PassiveSkillUtils.getTargetForCondition(fight, (FightObject)petFightObject, list, 1, true, 1);
          if (targets != null && targets.size() > 0)
            list.add(((FightObject)targets.get(0)).getId()); 
        } 
      } 
    } 
  }
}
