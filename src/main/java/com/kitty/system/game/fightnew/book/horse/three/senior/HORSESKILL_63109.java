package com.kitty.system.game.fightnew.book.horse.three.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_63109 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    if (effector.isPet()) {
      Integer hurt = (Integer)args[0];
      if (hurt == null)
        return null; 
      double wx_h = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
      if (wx_h >= 50.0D) {
        HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(63109));
        if (horseSkillParam == null)
          return null; 
        double percenta = (30.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0D) {
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 63109);
          skillRecord.setT(target.getId().intValue());
          NutMap map = NutMap.NEW();
          int alterHurt = (int)(hurt.intValue() * percenta);
          map.setv("alterHurt", Integer.valueOf(-alterHurt));
          return map;
        } 
      } 
    } 
    return null;
  }
}
