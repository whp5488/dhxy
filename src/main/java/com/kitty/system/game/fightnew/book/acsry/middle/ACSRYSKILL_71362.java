package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_71362 extends AcsrySkillProcessor {
  public NutMap addition(Role role, Object... args) {
    double percent = (role.getGg().intValue() / 300) * 0.01D;
    if (percent > 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), Double.valueOf(percent));
      return map;
    } 
    return null;
  }
}
