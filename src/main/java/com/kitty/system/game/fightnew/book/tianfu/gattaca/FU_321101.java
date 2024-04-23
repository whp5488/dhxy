package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;

public class FU_321101 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    NutMap map = NutMap.NEW();
    double percenta = 0.5D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
    if (percenta != 0.0D) {
      map.setv(String.valueOf(PRO_DICT_TYPE.F_KB_RATIO.getDist()), Double.valueOf(percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.L_KB_RATIO.getDist()), Double.valueOf(percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist()), Double.valueOf(percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.H_KB_RATIO.getDist()), Double.valueOf(percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist()), Double.valueOf(percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
}
