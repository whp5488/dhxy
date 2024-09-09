package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_322202 extends TianFuProcessor
{
    public FU_322202() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        NutMap map = NutMap.NEW();
        double percenta = 1.5 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percenta));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_DU.getDist()), Double.valueOf(percenta));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), Double.valueOf(percenta));
            map.setv(String.valueOf(PRO_DICT_TYPE.WL_XS.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
}
