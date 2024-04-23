package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;

public class FU_322104 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap wearInstantlyTakeEffect(Role role, Pet pet, TianFuAddition tianFuAddition, Object... obj) {
    NutMap map = NutMap.NEW();
    double percenta = 2.0D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
    if (percenta != 0.0D) {
      map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
}
