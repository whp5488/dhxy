package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31404 extends AwakenSkillProcessor {
  public NutMap countRecoverBloodEnd(Fight fight, FightObject effector, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 30.0D, 20.0D);
    if (map != null) {
      double percent = map.getDouble("percent");
      if (percent > 0.0D && SkillAi.canAffectOnPro(percent))
        ImbueUtil.addOrUpdSkill(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), 2, 31404, percent); 
    } 
    return null;
  }
}
