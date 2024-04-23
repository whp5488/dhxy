package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31422 extends AwakenSkillProcessor {
  public void beforePhysicalAttack(Fight fight, FightObject effector, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 30.0D, 20.0D);
    if (map == null)
      return; 
    double percent = map.getDouble("percent");
    if (percent > 0.0D && SkillAi.canAffectOnPro(percent)) {
      List<FightObject> mySide = fight.getMySide(effector.getId().intValue());
      int allPhyPower = 0;
      for (FightObject obj : mySide) {
        if (obj.isPet() && !obj.getId().equals(effector.getId()))
          allPhyPower += obj.getPhyPower(); 
      } 
      if (allPhyPower > 0) {
        double v = allPhyPower * 0.5D;
        double addPercent = v / effector.getPhyPower();
        ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.RISE_ATTACK.getDist()), 1, addPercent);
      } 
    } 
  }
}
