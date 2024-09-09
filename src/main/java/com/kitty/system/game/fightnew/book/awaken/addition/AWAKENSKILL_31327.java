package com.kitty.system.game.fightnew.book.awaken.addition;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31327 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31327() {
    }
    
    @Override
    public NutMap additionSuit(Role role, Pet pet, Object... args) {
        byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
        if (suitLevParam > 0) {
            double percent = this.getPercent((short)suitLevParam);
            NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percent));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap additionAwaken(Role role, Pet pet, Object... args) {
        short awaken = pet.getAwaken();
        if (awaken > 0) {
            double percent = this.getPercent(awaken);
            NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(percent));
            return map;
        }
        return null;
    }
    
    private double getPercent(short lev) {
        return (double)(20 * lev) / 20.0 / 100.0;
    }
}
