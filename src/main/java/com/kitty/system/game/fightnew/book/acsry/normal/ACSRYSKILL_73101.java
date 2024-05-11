
package com.kitty.system.game.fightnew.book.acsry.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73101 extends AcsrySkillProcessor
{
    @Override
    public NutMap addition(final Role role, final Object... args) {
        final double percent = 0.05;
        final NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.RISE_ZS.getDist()), (Object)percent);
        return map;
    }
}
