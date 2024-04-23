package com.kitty.system.game.fightnew.book.horse.one.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_61218 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    NutMap map = NutMap.NEW();
    double percent = HorseSkillUtils.getPercentMiddle(horseSkillParam);
    double percenta = (27.0D * percent + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta != 0.0D) {
      map.setv(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
}
