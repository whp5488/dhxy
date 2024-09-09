package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.List;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71322 extends AcsrySkillProcessor
{
    public ACSRYSKILL_71322() {
    }
    
    @Override
    public NutMap addition(Role role, Object... args) {
        try {
            List<Integer> acsrySkillList = (List)args[0];
            if (acsrySkillList == null || acsrySkillList.size() < 0) {
                return null;
            }
            if (acsrySkillList.contains(Integer.valueOf(71323))) {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
        double percent = (double)((int)role.getGg() / 300) * 0.01;
        if (percent > 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), Double.valueOf(percent));
            return map;
        }
        return null;
    }
}
