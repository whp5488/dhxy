
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321105 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public void beforeCountHurt(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (target != null && target.isPet() && args != null && args.length > 0) {
            final TianFuAddition tianFuAddition = (TianFuAddition)args[0];
            final double percenta = 1.0 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
            final double percentb = 0.15 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
            if (percenta != 0.0) {
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_F.getDist(), percenta);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_L.getDist(), percenta);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_S.getDist(), percenta);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_H.getDist(), percenta);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_GH.getDist(), percenta);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_SS.getDist(), percenta);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_ZS.getDist(), percenta);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), percenta);
            }
            if (percentb != 0.0) {
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.RISE_FY.getDist(), percentb);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.RISE_HL.getDist(), percentb);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.RISE_HS.getDist(), percentb);
                ImbueUtil.addTemp(effector.getImbueMap(), PRO_DICT_TYPE.RISE_YW.getDist(), percentb);
            }
        }
    }
}
