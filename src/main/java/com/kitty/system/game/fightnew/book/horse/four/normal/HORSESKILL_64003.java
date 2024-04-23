package com.kitty.system.game.fightnew.book.horse.four.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_64003 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percenta = getPercenta(horseSkillParam) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta != 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
  
  private double getPercenta(HorseSkillParam horseSkillParam) {
    double percent = (2.0E-4D * Math.pow(horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D), 2.0D) + 0.1D * horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D)) * 0.6D + 0.1D;
    return percent;
  }
}
