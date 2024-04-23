package com.kitty.system.game.fightnew.book.horse.one.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_61110 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percent = 0.2D;
    NutMap map = NutMap.NEW();
    map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(-percent));
    double percentb = (30.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    if (percentb != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()), Double.valueOf(percentb)); 
    return map;
  }
}
