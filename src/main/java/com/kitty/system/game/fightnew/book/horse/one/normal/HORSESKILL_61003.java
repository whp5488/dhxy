package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_61003 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    if (pet == null)
      return null; 
    NutMap map = NutMap.NEW();
    int maxMana = pet.getMaxMana().intValue();
    double percent = HorseSkillUtils.getPercent(horseSkillParam);
    double percenta = (10.0D * percent + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    int value = BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf(maxMana * percenta));
    if (value != 0) {
      map.setv(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist()), Integer.valueOf(value));
      return map;
    } 
    return null;
  }
}
