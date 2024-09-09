package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75203 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75203() {
    }
    
    @Override
    public NutMap addition(Role role, Object... args) {
        NutMap map = NutMap.NEW();
        double reducePercent = -0.05;
        map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(reducePercent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(reducePercent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(reducePercent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(reducePercent));
        double addPercent = 0.1;
        map.setv(PRO_DICT_TYPE.K_FY.getDist() + "_" + 119, Double.valueOf(addPercent));
        map.setv(PRO_DICT_TYPE.K_HL.getDist() + "_" + 119, Double.valueOf(addPercent));
        map.setv(PRO_DICT_TYPE.K_HS.getDist() + "_" + 119, Double.valueOf(addPercent));
        map.setv(PRO_DICT_TYPE.K_YW.getDist() + "_" + 119, Double.valueOf(addPercent));
        return map;
    }
}
