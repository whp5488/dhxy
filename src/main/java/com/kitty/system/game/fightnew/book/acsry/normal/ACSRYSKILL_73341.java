package com.kitty.system.game.fightnew.book.acsry.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73341 extends AcsrySkillProcessor
{
    public ACSRYSKILL_73341() {
    }
    
    @Override
    public NutMap addition(Role role, Object... args) {
        double percent = 0.08;
        NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.RISE_ZS.getDist()), Double.valueOf(percent));
        return map;
    }
}
