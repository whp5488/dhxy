package com.kitty.system.game.fightnew.book.acsry.normal;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75201 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75201() {
    }
    
    @Override
    public NutMap addition(Role role, Object... args) {
        double percent = 0.1;
        NutMap map = NutMap.NEW();
        map.setv(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), Double.valueOf(percent));
        map.setv(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percent));
        return map;
    }
}
