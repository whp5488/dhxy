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

public class HORSESKILL_61008 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percenta = (15.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    NutMap map = NutMap.NEW();
    if (percenta != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), Double.valueOf(percenta)); 
    int add = (int)(2.0D * horseSkillParam.getGrowUp() / 1000.0D / 1.325D * (0.6D * (-0.0067D * Math.pow(horseSkillParam.getLevel(), 2.0D) + 2.06D * horseSkillParam.getLevel()) / 130.0D + (-0.0067D * Math.pow(horseSkillParam.getProficient(), 2.0D) / 1000.0D + 2.06D * horseSkillParam.getProficient()) / 325000.0D + 0.1D) + 1.0D);
    if (add > 0)
      map.setv(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist()), Integer.valueOf(add)); 
    if (map.size() <= 0)
      return map; 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percent = HorseSkillUtils.getPercent(horseSkillParam);
    double percentb = (10.0D * percent + 0.1D) / 100.0D;
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    if (percentb != 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percentb));
      return map;
    } 
    return null;
  }
}
