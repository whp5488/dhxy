package com.kitty.system.game.fightnew.book.horse.one.normal;

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

public class HORSESKILL_61011 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    if (effector.getType() != 0)
      return null; 
    Integer hurt = (Integer)args[0];
    if (hurt == null)
      return null; 
    double wx_j = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_J.getDist());
    if (wx_j >= 50.0D) {
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(61011));
      if (horseSkillParam == null)
        return null; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 61011);
      double percenta = (40.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
      percenta = HorseSkillUtils.getMathRoundPercent(percenta);
      if (percenta != 0.0D) {
        NutMap map = NutMap.NEW();
        int alterHurt = (int)(hurt.intValue() * percenta);
        map.setv("alterHurt", Integer.valueOf(alterHurt));
        skillRecord.setT(target.getId().intValue());
        return map;
      } 
    } 
    return null;
  }
}
