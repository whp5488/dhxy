
package com.kitty.system.game.fightnew.book.acsry.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75101 extends AcsrySkillProcessor
{
    @Override
    public NutMap additionWear(final Role role, final Object... args) {
        final double percent = 0.1;
        final NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), (Object)percent);
        return map;
    }
}
