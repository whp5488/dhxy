package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class FU_323102 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap otherAddition(Pet pet, TianFuAddition tianFuAddition, Object... args) {
    NutMap map = NutMap.NEW();
    int ll = (pet.getLl() == null) ? 0 : pet.getLl().intValue();
    int flag = ll / 100;
    if (flag > 0) {
      double percenta = 0.05D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
      map.setv(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), Double.valueOf(flag * percenta));
      return map;
    } 
    return null;
  }
}
