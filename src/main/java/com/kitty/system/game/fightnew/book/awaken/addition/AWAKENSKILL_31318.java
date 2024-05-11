
package com.kitty.system.game.fightnew.book.awaken.addition;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31318 extends AwakenSkillProcessor
{
    @Override
    public NutMap additionSuit(final Role role, final Pet pet, final Object... args) {
        final byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
        if (suitLevParam > 0) {
            final double percent = this.getPercent(suitLevParam);
            final NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), (Object)percent);
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap additionAwaken(final Role role, final Pet pet, final Object... args) {
        final short awaken = pet.getAwaken();
        if (awaken > 0) {
            final double percent = this.getPercent(awaken);
            final NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), (Object)percent);
            return map;
        }
        return null;
    }
    
    private double getPercent(final short lev) {
        return 20 * lev / 20.0 / 100.0;
    }
}
