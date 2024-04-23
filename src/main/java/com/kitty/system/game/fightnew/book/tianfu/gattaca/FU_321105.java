package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import org.nutz.lang.util.NutMap;

public class FU_321105 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void beforeCountHurt(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (target != null && target.isPet() && args != null && args.length > 0) {
      TianFuAddition tianFuAddition = (TianFuAddition)args[0];
      double percenta = 1.0D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
      double percentb = 0.15D * TianFuUtils.getCountAddition(tianFuAddition) / 100.0D;
      if (percenta != 0.0D) {
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_F.getDist(), percenta);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_L.getDist(), percenta);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_S.getDist(), percenta);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_H.getDist(), percenta);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_GH.getDist(), percenta);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_SS.getDist(), percenta);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_ZS.getDist(), percenta);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), percenta);
      } 
      if (percentb != 0.0D) {
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.RISE_FY.getDist(), percentb);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.RISE_HL.getDist(), percentb);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.RISE_HS.getDist(), percentb);
        ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.RISE_YW.getDist(), percentb);
      } 
    } 
  }
}
