package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_75202 extends AcsrySkillProcessor {
  public NutMap addition(Role role, Object... args) {
    try {
      List<Integer> acsrySkillList = (List<Integer>)args[0];
      if (acsrySkillList == null || acsrySkillList.size() < 0)
        return null; 
      if (acsrySkillList.contains(Integer.valueOf(75203)))
        return null; 
    } catch (Exception e) {
      return null;
    } 
    NutMap map = NutMap.NEW();
    double reducePercent = -0.05D;
    map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(reducePercent));
    map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(reducePercent));
    map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(reducePercent));
    map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(reducePercent));
    double addPercent = 0.05D;
    map.setv(PRO_DICT_TYPE.K_FY.getDist() + "_" + 'w', Double.valueOf(addPercent));
    map.setv(PRO_DICT_TYPE.K_HL.getDist() + "_" + 'w', Double.valueOf(addPercent));
    map.setv(PRO_DICT_TYPE.K_HS.getDist() + "_" + 'w', Double.valueOf(addPercent));
    map.setv(PRO_DICT_TYPE.K_YW.getDist() + "_" + 'w', Double.valueOf(addPercent));
    return map;
  }
}
