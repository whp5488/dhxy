
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.List;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71322 extends AcsrySkillProcessor
{
    @Override
    public NutMap addition(final Role role, final Object... args) {
        try {
            final List<Integer> acsrySkillList = (List<Integer>)args[0];
            if (acsrySkillList == null || acsrySkillList.size() < 0) {
                return null;
            }
            if (acsrySkillList.contains(71323)) {
                return null;
            }
        }
        catch (final Exception e) {
            return null;
        }
        final double percent = role.getGg() / 300 * 0.01;
        if (percent > 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), (Object)percent);
            return map;
        }
        return null;
    }
}
