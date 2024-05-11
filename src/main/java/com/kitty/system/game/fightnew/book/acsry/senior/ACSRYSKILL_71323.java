
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71323 extends AcsrySkillProcessor
{
    @Override
    public NutMap addition(final Role role, final Object... args) {
        double percent = role.getGg() / 200 * 0.01;
        percent += role.getMj() / 400 * 0.01;
        if (percent > 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), (Object)percent);
            return map;
        }
        return null;
    }
}
