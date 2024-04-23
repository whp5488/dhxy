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

public class HORSESKILL_63016 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percenta = ((2.0E-4D * Math.pow(horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D), 2.0D) + 0.1D * horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D)) * 0.6D + 0.1D) / 100.0D;
    double percentb = (10.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    NutMap map = NutMap.NEW();
    if (percenta != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(percenta)); 
    if (percentb != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), Double.valueOf(percentb)); 
    if (map.size() > 0)
      return map; 
    return null;
  }
}
