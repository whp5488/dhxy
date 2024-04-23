package com.kitty.system.game.fightnew.book.acsry.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_75301 extends AcsrySkillProcessor {
  public NutMap addition(Role role, Object... args) {
    double percent = 0.025D;
    NutMap map = NutMap.NEW();
    map.setv(PRO_DICT_TYPE.K_FY.getDist() + "_" + 'w', Double.valueOf(percent));
    map.setv(PRO_DICT_TYPE.K_HL.getDist() + "_" + 'w', Double.valueOf(percent));
    map.setv(PRO_DICT_TYPE.K_HS.getDist() + "_" + 'w', Double.valueOf(percent));
    map.setv(PRO_DICT_TYPE.K_YW.getDist() + "_" + 'w', Double.valueOf(percent));
    return map;
  }
}
