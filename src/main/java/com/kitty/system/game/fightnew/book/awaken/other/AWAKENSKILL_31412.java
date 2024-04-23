package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.Arrays;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31412 extends AwakenSkillProcessor {
  public void sufferHarm(Fight fight, FightObject fightObject, Object... args) {
    if (args == null || args.length <= 0 || fightObject.getSkillCoolMap().getInt(String.valueOf(31412)) > 0)
      return; 
    int hurt = ((Integer)args[0]).intValue();
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 50.0D, 20.0D);
    if (map == null)
      return; 
    double percent = map.getDouble("percent");
    if (percent > 0.0D && SkillAi.canAffectOnPro(percent)) {
      int life = (int)(hurt * 0.3D);
      if (life > 0) {
        life = (life > 200000) ? 200000 : life;
        FightObject unit = SkillAi.effectProBass(fight, fightObject, Integer.valueOf(1), Arrays.asList(new String[] { PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue() }));
        if (unit != null) {
          ActionParamUtils.changeLife(fight, fightObject, unit, 7, 31412, 2, life, false);
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 31412);
          skillRecord.setT(unit.getId().intValue());
          fightObject.getSkillCoolMap().setv(String.valueOf(31412), Integer.valueOf(1));
        } 
      } 
    } 
  }
}
