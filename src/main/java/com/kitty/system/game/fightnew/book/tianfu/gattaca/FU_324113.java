package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324113 extends TianFuProcessor
{
    public FU_324113() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        NutMap map = NutMap.NEW();
        double percenta = 0.5 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
        if (percenta != 0.0) {
            map.setv(String.valueOf(PRO_DICT_TYPE.RISE_HL.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
}
