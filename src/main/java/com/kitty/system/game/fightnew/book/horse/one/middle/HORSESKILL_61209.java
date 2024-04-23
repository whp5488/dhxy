package com.kitty.system.game.fightnew.book.horse.one.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_61209 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percent = HorseSkillUtils.getPercentMiddle(horseSkillParam);
    double percentb = (7.0D * percent + 0.1D) / 100.0D;
    double percentd = (15.0D * percent + 0.1D) / 100.0D;
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    percentd = HorseSkillUtils.getMathRoundPercent(percentd);
    NutMap map = NutMap.NEW();
    if (percentb != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), Double.valueOf(percentb)); 
    if (percentd != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), Double.valueOf(percentd)); 
    if (map.size() > 0)
      return map; 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percent = HorseSkillUtils.getPercentMiddle(horseSkillParam);
    double percenta = (8.0D * percent + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta != 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
}
