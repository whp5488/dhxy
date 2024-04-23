package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;

public class FU_322402 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    NutMap map = NutMap.NEW();
    double percenta = 1.0D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
    if (percenta != 0.0D) {
      map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()) + "_" + 'w', Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
}
