package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74312 extends AcsrySkillProcessor
{
    public ACSRYSKILL_74312() {
    }
    
    @Override
    public NutMap addition(Role role, Object... args) {
        double percent = (double)((int)role.getGg() / 300) * 0.01;
        if (percent > 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), Double.valueOf(percent));
            return map;
        }
        return null;
    }
}
