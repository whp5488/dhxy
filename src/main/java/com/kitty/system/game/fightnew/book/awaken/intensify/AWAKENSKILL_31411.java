package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31411 extends AwakenSkillProcessor {
  public NutMap skillIntensify(Fight fight, PetFightObject petFightObject, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 10.0D, 20.0D);
    if (map != null) {
      double percent = map.getDouble("percent");
      if (percent > 0.0D)
        map.put("percent", Double.valueOf(percent * 100.0D)); 
    } 
    return map;
  }
}
