package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;

public class FU_324317 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    NutMap map = NutMap.NEW();
    double percenta = 0.5D * tianFuAddition.getTianFuLevel() / 100.0D;
    double percentb = 0.5D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
    double percentc = 0.5D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), Double.valueOf(percenta));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), Double.valueOf(percenta));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HS.getDist()), Double.valueOf(percenta));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), Double.valueOf(percenta));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(percentb));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(percentb));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(percentb));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(percentb));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist()), Double.valueOf(percentb));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(percentb));
    map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist()), Double.valueOf(percentc));
    return map;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap wearInstantlyTakeEffect(Role role, Pet pet, TianFuAddition tianFuAddition, Object... obj) {
    NutMap map = NutMap.NEW();
    map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), Double.valueOf(-0.1D));
    return map;
  }
}
