package com.kitty.system.game.fightnew.book.horse.three.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_63013 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    NutMap map = NutMap.NEW();
    double percentb = (6.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    double percentc = (10.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    percentc = HorseSkillUtils.getMathRoundPercent(percentc);
    if (percentb != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(percentb)); 
    if (percentc != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(percentc)); 
    if (map.size() > 0)
      return map; 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percenta = (15.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta != 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
}
