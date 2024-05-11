
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74332 extends AcsrySkillProcessor
{
    @Override
    public NutMap addition(final Role role, final Object... args) {
        final double percent = role.getGg() / 300 * 0.01;
        if (percent > 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), (Object)percent);
            return map;
        }
        return null;
    }
}
