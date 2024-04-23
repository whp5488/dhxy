package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class FU_323401 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap otherAddition(Pet pet, TianFuAddition tianFuAddition, Object... args) {
    NutMap map = NutMap.NEW();
    int gg = (pet.getGg() == null) ? 0 : pet.getGg().intValue();
    int lx = (pet.getLx() == null) ? 0 : pet.getLx().intValue();
    int flag = 0;
    int ggflag = gg / 100;
    if (ggflag > 0)
      flag += ggflag; 
    int lxflag = lx / 100;
    if (lxflag > 0)
      flag += lxflag; 
    if (flag > 0) {
      double percenta = 0.1D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
      map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(percenta * flag));
      map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(percenta * flag));
      map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(percenta * flag));
      map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(percenta * flag));
      return map;
    } 
    return null;
  }
}
