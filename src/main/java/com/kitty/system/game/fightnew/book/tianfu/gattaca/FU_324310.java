package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324310 extends TianFuProcessor
{
    public FU_324310() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        NutMap map = NutMap.NEW();
        double percent2 = 0.3 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), Double.valueOf(percent2));
        return map;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public NutMap wearInstantlyTakeEffect(Role role, Pet pet, TianFuAddition tianFuAddition, Object... obj) {
        NutMap map = NutMap.NEW();
        double percent1 = (double)(1 * tianFuAddition.getTianFuLevel()) / 100.0;
        map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), Double.valueOf(-percent1));
        return map;
    }
}
