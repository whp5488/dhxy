
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324311 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        final NutMap map = NutMap.NEW();
        final double percent1 = 1 * tianFuAddition.getTianFuLevel() / 100.0;
        final double percent2 = 0.3 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
        map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), (Object)(-percent1));
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), (Object)percent2);
        return map;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
}
