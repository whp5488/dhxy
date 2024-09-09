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
    public FU_323402() {
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
        int level = (pet.getPetLv() == null) ? 0 : ((int)pet.getPetLv());
        if ((int)pet.getRebirth() < 3 || level < 120) {
            return null;
        }
        if (level > 180) {
            level = 180;
        }
        int flag = (level - 120) / 10;
        if (flag > 0) {
            NutMap map = NutMap.NEW();
            double percenta = 0.1 * TianFuUtils.getCountAddition(tianFuAddition) / 100.0;
            map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(percenta * (double)flag));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(percenta * (double)flag));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(percenta * (double)flag));
            map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(percenta * (double)flag));
            return map;
        }
        return null;
    }
}
