
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323401 extends TianFuProcessor
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
    public NutMap otherAddition(final Pet pet, final TianFuAddition tianFuAddition, final Object... args) {
        final NutMap map = NutMap.NEW();
        final int gg = (pet.getGg() == null) ? 0 : pet.getGg();
        final int lx = (pet.getLx() == null) ? 0 : pet.getLx();
        int flag = 0;
        final int ggflag = gg / 100;
        if (ggflag > 0) {
            flag += ggflag;
        }
        final int lxflag = lx / 100;
        if (lxflag > 0) {
            flag += lxflag;
        }
        if (flag > 0) {
            final double percenta = 0.1 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
            map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)(percenta * flag));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)(percenta * flag));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), (Object)(percenta * flag));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)(percenta * flag));
            return map;
        }
        return null;
    }
}
