package com.kitty.system.game.fightnew.book.acsry.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71301 extends AcsrySkillProcessor
{
    public ACSRYSKILL_71301() {
    }
    
    @Override
    public NutMap addition(Role role, Object... args) {
        double percent = 0.1;
        NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.RISE_HL.getDist()), Double.valueOf(percent));
        return map;
    }
}
