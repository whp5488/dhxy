
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.List;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75202 extends AcsrySkillProcessor
{
    @Override
    public NutMap addition(final Role role, final Object... args) {
        try {
            final List<Integer> acsrySkillList = (List<Integer>)args[0];
            if (acsrySkillList == null || acsrySkillList.size() < 0) {
                return null;
            }
            if (acsrySkillList.contains(75203)) {
                return null;
            }
        }
        catch (final Exception e) {
            return null;
        }
        final NutMap map = NutMap.NEW();
        final double reducePercent = -0.05;
        map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)reducePercent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)reducePercent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), (Object)reducePercent);
        map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)reducePercent);
        final double addPercent = 0.05;
        map.setv(PRO_DICT_TYPE.K_FY.getDist() + "_" + 119, (Object)addPercent);
        map.setv(PRO_DICT_TYPE.K_HL.getDist() + "_" + 119, (Object)addPercent);
        map.setv(PRO_DICT_TYPE.K_HS.getDist() + "_" + 119, (Object)addPercent);
        map.setv(PRO_DICT_TYPE.K_YW.getDist() + "_" + 119, (Object)addPercent);
        return map;
    }
}
