package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71342 extends AcsrySkillProcessor
{
    public ACSRYSKILL_71342() {
    }
    
    @Override
    public NutMap addition(Role role, Object... args) {
        double percent = (double)((int)role.getGg() / 300) * 0.01;
        if (percent > 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_HS.getDist()), Double.valueOf(percent));
            return map;
        }
        return null;
    }
}
