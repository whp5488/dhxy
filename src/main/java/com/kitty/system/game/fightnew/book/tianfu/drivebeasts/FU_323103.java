package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323103 extends TianFuProcessor
{
    public FU_323103() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public NutMap otherAddition(Pet pet, TianFuAddition tianFuAddition, Object... args) {
        NutMap map = NutMap.NEW();
        int ll = (pet.getLl() == null) ? 0 : ((int)pet.getLl());
        int flag = ll / 100;
        if (flag > 0) {
            double percenta = 0.2 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
            map.setv(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), Double.valueOf((double)flag * percenta));
            return map;
        }
        return null;
    }
}
