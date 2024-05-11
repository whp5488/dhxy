
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321304 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        final NutMap map = NutMap.NEW();
        final double percenta = 0.1 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
        final double percentb = 0.8 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
        final double percentc = 1.6 * tianFuAddition.getDayInCeLevel();
        final double percentd = 0.8 * tianFuAddition.getDayInCeLevel();
        map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), (Object)(-0.3));
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), (Object)percenta);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), (Object)percenta);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HS.getDist()), (Object)percenta);
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), (Object)percenta);
        }
        if (percentb != 0.0) {
            map.setv(PRO_DICT_TYPE.OMIT_K_F.getDist() + "_" + 121, (Object)percentb);
            map.setv(PRO_DICT_TYPE.OMIT_K_L.getDist() + "_" + 121, (Object)percentb);
            map.setv(PRO_DICT_TYPE.OMIT_K_S.getDist() + "_" + 121, (Object)percentb);
            map.setv(PRO_DICT_TYPE.OMIT_K_H.getDist() + "_" + 121, (Object)percentb);
            map.setv(PRO_DICT_TYPE.OMIT_K_GH.getDist() + "_" + 121, (Object)percentb);
            map.setv(PRO_DICT_TYPE.OMIT_K_SS.getDist() + "_" + 121, (Object)percentb);
            map.setv(PRO_DICT_TYPE.OMIT_K_D.getDist() + "_" + 121, (Object)percentb);
            map.setv(PRO_DICT_TYPE.OMIT_K_ZS.getDist() + "_" + 121, (Object)percentb);
            map.setv(PRO_DICT_TYPE.OMIT_K_F.getDist() + "_" + 19, (Object)percentc);
            map.setv(PRO_DICT_TYPE.OMIT_K_L.getDist() + "_" + 19, (Object)percentc);
            map.setv(PRO_DICT_TYPE.OMIT_K_S.getDist() + "_" + 19, (Object)percentc);
            map.setv(PRO_DICT_TYPE.OMIT_K_H.getDist() + "_" + 19, (Object)percentc);
            map.setv(PRO_DICT_TYPE.OMIT_K_GH.getDist() + "_" + 19, (Object)percentc);
            map.setv(PRO_DICT_TYPE.OMIT_K_SS.getDist() + "_" + 19, (Object)percentc);
            map.setv(PRO_DICT_TYPE.OMIT_K_D.getDist() + "_" + 19, (Object)percentc);
            map.setv(PRO_DICT_TYPE.OMIT_K_ZS.getDist() + "_" + 19, (Object)percentd);
        }
        if (map.size() <= 0) {
            return null;
        }
        return map;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
}
