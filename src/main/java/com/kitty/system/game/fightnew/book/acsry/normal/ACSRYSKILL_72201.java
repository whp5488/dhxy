
package com.kitty.system.game.fightnew.book.acsry.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_72201 extends AcsrySkillProcessor
{
    @Override
    public NutMap addition(final Role role, final Object... args) {
        final double percent = 0.08;
        final NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.RISE_F.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.RISE_L.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.RISE_S.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.RISE_H.getDist()), (Object)percent);
        map.setv(String.valueOf(PRO_DICT_TYPE.RISE_GH.getDist()), (Object)percent);
        return map;
    }
}
