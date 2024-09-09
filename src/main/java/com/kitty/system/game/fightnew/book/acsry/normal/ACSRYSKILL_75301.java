package com.kitty.system.game.fightnew.book.acsry.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75301 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75301() {
    }
    
    @Override
    public NutMap addition(Role role, Object... args) {
        double percent = 0.025;
        NutMap map = NutMap.NEW();
        map.setv(PRO_DICT_TYPE.K_FY.getDist() + "_" + 119, Double.valueOf(percent));
        map.setv(PRO_DICT_TYPE.K_HL.getDist() + "_" + 119, Double.valueOf(percent));
        map.setv(PRO_DICT_TYPE.K_HS.getDist() + "_" + 119, Double.valueOf(percent));
        map.setv(PRO_DICT_TYPE.K_YW.getDist() + "_" + 119, Double.valueOf(percent));
        return map;
    }
}
