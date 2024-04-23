package com.kitty.system.game.fightnew.book.horse.two.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_62119 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percentc = (13.5D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
    percentc = HorseSkillUtils.getMathRoundPercent(percentc);
    if (percentc > 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(percentc));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(percentc));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(percentc));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(percentc));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(percentc));
      return map;
    } 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percenta = (2.0E-4D * Math.pow(horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D), 2.0D) + 0.1D * horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D) + 0.2D) / 100.0D;
    double percentb = (10.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    NutMap map = NutMap.NEW();
    if (percenta != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(percenta)); 
    if (percentb != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), Double.valueOf(percentb)); 
    if (map.size() > 0)
      return map; 
    return null;
  }
}
