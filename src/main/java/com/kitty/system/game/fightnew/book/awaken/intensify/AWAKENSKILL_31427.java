package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31427 extends AwakenSkillProcessor {
  public NutMap skillIntensify(Fight fight, PetFightObject petFightObject, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 50.0D, 20.0D);
    if (map == null)
      return null; 
    double percent = map.getDouble("percent");
    if (percent != 0.0D)
      ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), 1, percent); 
    return null;
  }
}
