package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_61002 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    NutMap map = NutMap.NEW();
    double percent = HorseSkillUtils.getPercent(horseSkillParam);
    double percenta = (5.0D * percent + 0.1D) / 100.0D;
    double percentb = (35.0D * percent + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    percentb = HorseSkillUtils.getMathRoundPercent(percentb);
    if (percenta != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), Double.valueOf(percenta)); 
    if (percentb != 0.0D)
      map.setv(String.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), Double.valueOf(percenta)); 
    if (map.size() <= 0)
      return map; 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
}
