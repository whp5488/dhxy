
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323402 extends TianFuProcessor
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
        int level = (pet.getPetLv() == null) ? 0 : pet.getPetLv();
        if (pet.getRebirth() < 3 || level < 120) {
            return null;
        }
        if (level > 180) {
            level = 180;
        }
        final int flag = (level - 120) / 10;
        if (flag > 0) {
            final NutMap map = NutMap.NEW();
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
