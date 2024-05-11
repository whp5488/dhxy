
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75203 extends AcsrySkillProcessor
{
    @Override
    public NutMap addition(final Role role, final Object... args) {
        final NutMap map = NutMap.NEW();
        final double reducePercent = -0.05;
        map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)reducePercent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)reducePercent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), (Object)reducePercent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)reducePercent);
        final double addPercent = 0.1;
        map.setv(PRO_DICT_TYPE.K_FY.getDist() + "_" + 119, (Object)addPercent);
        map.setv(PRO_DICT_TYPE.K_HL.getDist() + "_" + 119, (Object)addPercent);
        map.setv(PRO_DICT_TYPE.K_HS.getDist() + "_" + 119, (Object)addPercent);
        map.setv(PRO_DICT_TYPE.K_YW.getDist() + "_" + 119, (Object)addPercent);
        return map;
    }
}
