package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;

public class FU_323501 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap wearInstantlyTakeEffect(Role role, Pet pet, TianFuAddition tianFuAddition, Object... obj) {
    if (pet == null || tianFuAddition == null)
      return null; 
    int level = pet.getPetLv().intValue();
    if (pet.getRebirth().intValue() < 3 || level < 120)
      return null; 
    if (level > 180)
      level = 180; 
    int flag = (level - 120) / 10;
    if (flag > 0) {
      NutMap map = NutMap.NEW();
      double cnta = 5.0E-4D * TianFuUtils.getCountAddition(tianFuAddition);
      map.setv(String.valueOf(PRO_DICT_TYPE.CZL.getDist()), Double.valueOf(cnta * flag));
      return map;
    } 
    return null;
  }
}
