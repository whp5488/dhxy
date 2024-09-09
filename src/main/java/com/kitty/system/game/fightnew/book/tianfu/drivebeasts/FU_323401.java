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
    public FU_323401() {
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
        int gg = (pet.getGg() == null) ? 0 : ((int)pet.getGg());
        int lx = (pet.getLx() == null) ? 0 : ((int)pet.getLx());
        int flag = 0;
        int ggflag = gg / 100;
        if (ggflag > 0) {
            flag += ggflag;
        }
        int lxflag = lx / 100;
        if (lxflag > 0) {
            flag += lxflag;
        }
        if (flag > 0) {
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
