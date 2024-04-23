package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_61021 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percentc = (16.8D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    double percentd = (8.4D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percentc = HorseSkillUtils.getMathRoundPercent(percentc);
    percentd = HorseSkillUtils.getMathRoundPercent(percentd);
    NutMap map = NutMap.NEW();
    if (percentc != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), Double.valueOf(percentc)); 
    if (percentd != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percentd)); 
    if (map.size() > 0)
      return map; 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    NutMap map = NutMap.NEW();
    double percenta = (4.2D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    double percentb = (12.5D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    if (percenta != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), Double.valueOf(percenta)); 
    if (percentb != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percentb)); 
    if (map.size() > 0)
      return map; 
    return null;
  }
}
