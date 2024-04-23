package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31503 extends AwakenSkillProcessor {
  public void sufferHarm(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown() || fightObject.getSkillCoolMap().getInt(String.valueOf(31503)) > 0)
      return; 
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 50.0D, 20.0D);
    if (map == null)
      return; 
    double percenta = map.getDouble("percent");
    if (percenta > 0.0D && SkillAi.canAffectOnPro(percenta)) {
      map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 75.0D, 20.0D);
      double percentb = map.getDouble("percent");
      if (percentb > 0.0D) {
        int life = (int)(fightObject.getMaxLife() * percentb);
        if (life > 0) {
          ActionParamUtils.changeLife(fight, fightObject, fightObject, 7, 31503, 2, life, false);
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 31503);
          skillRecord.setT(fightObject.getId().intValue());
          fightObject.getSkillCoolMap().setv(String.valueOf(31503), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
        } 
      } 
    } 
  }
}
