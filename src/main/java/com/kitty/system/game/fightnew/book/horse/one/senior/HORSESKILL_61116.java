package com.kitty.system.game.fightnew.book.horse.one.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_61116 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    if (effector.getType() != 0)
      return null; 
    if (args != null && args.length > 2) {
      Boolean isFury = (Boolean)args[1];
      if (isFury == null || !isFury.booleanValue())
        return null; 
      Integer hurt = (Integer)args[0];
      if (hurt == null)
        return null; 
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(61116));
      if (horseSkillParam == null)
        return null; 
      double percenta = (50.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
      percenta = HorseSkillUtils.getMathRoundPercent(percenta);
      if (percenta != 0.0D) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 61116);
        skillRecord.setT(effector.getId().intValue());
        NutMap map = NutMap.NEW();
        int alterHurt = (int)(hurt.intValue() * percenta);
        map.setv("alterHurt", Integer.valueOf(alterHurt));
        return map;
      } 
    } 
    return null;
  }
}
