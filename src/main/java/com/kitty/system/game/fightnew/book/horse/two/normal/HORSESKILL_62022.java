package com.kitty.system.game.fightnew.book.horse.two.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_62022 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    double percentb = (16.8D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    double percentc = (8.4D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    double percentd = (7.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    double percente = (29.25D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    percentc = HorseSkillUtils.getMathRoundPercent(percentc);
    percentd = HorseSkillUtils.getMathRoundPercent(percentd);
    percente = HorseSkillUtils.getMathRoundPercent(percente);
    NutMap map = NutMap.NEW();
    if (percentb != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), Double.valueOf(percentb)); 
    if (percentc != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percentb)); 
    if (percentd != 0.0D) {
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(percentb));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(percentb));
    } 
    if (percente != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist()), Double.valueOf(percente)); 
    if (map.size() > 0)
      return map; 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percenta = (18.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta != 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
}
