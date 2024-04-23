package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;

public class FU_324308 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    NutMap map = NutMap.NEW();
    double percent1 = (1 * tianFuAddition.getTianFuLevel()) / 100.0D;
    double percent2 = 1.5D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
    map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(-percent1));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), Double.valueOf(percent2));
    return map;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
}
