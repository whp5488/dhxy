package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31414 extends AwakenSkillProcessor {
  public void enterFight(Fight fight, FightObject fightObject, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 25.0D, 20.0D);
    if (map == null)
      return; 
    double percent = map.getDouble("percent");
    ImbueUtil.addRound(fightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), 1, percent + 0.1D);
    ImbueUtil.addRound(fightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), 2, -0.1D);
  }
}
