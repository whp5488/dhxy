package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31312 extends AwakenSkillProcessor {
  public void phyAttackEnd(Fight fight, FightObject effector, Object... args) {
    if (args == null || args.length < 2)
      return; 
    BattleUnit battleUnit = (BattleUnit)args[0];
    ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
    if (battleUnit == null || actionDetailParam == null)
      return; 
    FightObject target = fight.queryFightObject(battleUnit.getTarget().getId());
    if (target == null || target.isFallDown())
      return; 
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 5.0D, 20.0D);
    if (map == null)
      return; 
    double percent = map.getDouble("percent");
    if (percent > 0.0D && 
      SkillAi.canAffectOnPro(percent)) {
      int addSd = 0;
      if (target.getSpeed() != 0)
        addSd = (int)(target.getSpeed() * 0.25D); 
      if (addSd > 0) {
        ImbueUtil.addRound(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist()), 1, -addSd);
      } else if (addSd < 0) {
        ImbueUtil.addRound(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist()), 1, addSd);
      } 
    } 
  }
}
