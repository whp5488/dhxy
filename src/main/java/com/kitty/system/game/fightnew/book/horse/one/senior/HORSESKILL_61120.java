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

public class HORSESKILL_61120 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    if (effector.getType() != 0)
      return null; 
    if (target.getStatusValue(1002) > 0 || target.getStatusValue(1004) > 0 || target
      .getStatusValue(1006) > 0) {
      Integer hurt = (Integer)args[0];
      if (hurt.intValue() <= 0)
        return null; 
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(61120));
      if (horseSkillParam == null)
        return null; 
      double percenta = (40.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
      percenta = HorseSkillUtils.getMathRoundPercent(percenta);
      if (percenta != 0.0D) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 61120);
        skillRecord.setT(target.getId().intValue());
        int alterHurt = (int)(hurt.intValue() * percenta);
        NutMap map = NutMap.NEW();
        map.setv("alterHurt", Integer.valueOf(alterHurt));
        return map;
      } 
    } 
    return null;
  }
}
