package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;

public class FU_321304 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    NutMap map = NutMap.NEW();
    double percenta = 0.1D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
    double percentb = 0.8D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
    double percentc = 1.6D * tianFuAddition.getDayInCeLevel();
    double percentd = 0.8D * tianFuAddition.getDayInCeLevel();
    map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(-0.3D));
    if (percenta != 0.0D) {
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), Double.valueOf(percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), Double.valueOf(percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HS.getDist()), Double.valueOf(percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), Double.valueOf(percenta));
    } 
    if (percentb != 0.0D) {
      map.setv(PRO_DICT_TYPE.OMIT_K_F.getDist() + "_" + 'y', Double.valueOf(percentb));
      map.setv(PRO_DICT_TYPE.OMIT_K_L.getDist() + "_" + 'y', Double.valueOf(percentb));
      map.setv(PRO_DICT_TYPE.OMIT_K_S.getDist() + "_" + 'y', Double.valueOf(percentb));
      map.setv(PRO_DICT_TYPE.OMIT_K_H.getDist() + "_" + 'y', Double.valueOf(percentb));
      map.setv(PRO_DICT_TYPE.OMIT_K_GH.getDist() + "_" + 'y', Double.valueOf(percentb));
      map.setv(PRO_DICT_TYPE.OMIT_K_SS.getDist() + "_" + 'y', Double.valueOf(percentb));
      map.setv(PRO_DICT_TYPE.OMIT_K_D.getDist() + "_" + 'y', Double.valueOf(percentb));
      map.setv(PRO_DICT_TYPE.OMIT_K_ZS.getDist() + "_" + 'y', Double.valueOf(percentb));
      map.setv(PRO_DICT_TYPE.OMIT_K_F.getDist() + "_" + '\023', Double.valueOf(percentc));
      map.setv(PRO_DICT_TYPE.OMIT_K_L.getDist() + "_" + '\023', Double.valueOf(percentc));
      map.setv(PRO_DICT_TYPE.OMIT_K_S.getDist() + "_" + '\023', Double.valueOf(percentc));
      map.setv(PRO_DICT_TYPE.OMIT_K_H.getDist() + "_" + '\023', Double.valueOf(percentc));
      map.setv(PRO_DICT_TYPE.OMIT_K_GH.getDist() + "_" + '\023', Double.valueOf(percentc));
      map.setv(PRO_DICT_TYPE.OMIT_K_SS.getDist() + "_" + '\023', Double.valueOf(percentc));
      map.setv(PRO_DICT_TYPE.OMIT_K_D.getDist() + "_" + '\023', Double.valueOf(percentc));
      map.setv(PRO_DICT_TYPE.OMIT_K_ZS.getDist() + "_" + '\023', Double.valueOf(percentd));
    } 
    if (map.size() <= 0)
      return null; 
    return map;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
}
