
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

public class FU_324317 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        final NutMap map = NutMap.NEW();
        final double percenta = 0.5 * tianFuAddition.getTianFuLevel() / 100.0;
        final double percentb = 0.5 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
        final double percentc = 0.5 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), (Object)percenta);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), (Object)percenta);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HS.getDist()), (Object)percenta);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), (Object)percenta);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), (Object)percentb);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), (Object)percentb);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), (Object)percentb);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), (Object)percentb);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist()), (Object)percentb);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), (Object)percentb);
        map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist()), (Object)percentc);
        return map;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap wearInstantlyTakeEffect(final Role role, final Pet pet, final TianFuAddition tianFuAddition, final Object... obj) {
        final NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), (Object)(-0.1));
        return map;
    }
}
