package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31501 extends AwakenSkillProcessor {
  public NutMap skillIntensify(Fight fight, PetFightObject petFightObject, Object... args) {
    return AwakenSkillEffectUtils.getPercentDouble(petFightObject, 70.0D, 20.0D);
  }
}
