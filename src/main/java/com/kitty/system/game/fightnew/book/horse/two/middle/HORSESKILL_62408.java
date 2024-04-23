package com.kitty.system.game.fightnew.book.horse.two.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_62408 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percentb = (7.0D * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1D) / 100.0D;
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    if (percentb != 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percentb));
      return map;
    } 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percent = 0.2D;
    NutMap map = NutMap.NEW();
    map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(-percent));
    double percenta = (35.0D * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta > 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), Double.valueOf(percenta)); 
    return map;
  }
}
