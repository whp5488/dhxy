package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class FU_323402 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap otherAddition(Pet pet, TianFuAddition tianFuAddition, Object... args) {
    int level = (pet.getPetLv() == null) ? 0 : pet.getPetLv().intValue();
    if (pet.getRebirth().intValue() < 3 || level < 120)
      return null; 
    if (level > 180)
      level = 180; 
    int flag = (level - 120) / 10;
    if (flag > 0) {
      NutMap map = NutMap.NEW();
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
