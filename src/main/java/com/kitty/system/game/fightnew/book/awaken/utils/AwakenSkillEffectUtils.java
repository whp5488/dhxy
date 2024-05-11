
package com.kitty.system.game.fightnew.book.awaken.utils;

import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.bean.PetFightObject;

public class AwakenSkillEffectUtils
{
    public static NutMap getPercentDouble(final PetFightObject petFightObject, final double param1, final double param2) {
        final Role role = petFightObject.getRole();
        final Pet pet = petFightObject.getPet();
        if (role == null || pet == null) {
            return null;
        }
        final byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
        if (suitLevParam > 0) {
            double percent = getPercent(suitLevParam, param1, param2);
            if (pet.getAwaken() > 0) {
                percent += getPercent(pet.getAwaken(), param1, param2);
            }
            return NutMap.NEW().setv("percent", (Object)percent);
        }
        return null;
    }
    
    private static double getPercent(final int param, final double param1, final double param2) {
        return param1 * param / param2 / 100.0;
    }
    
    public static int getStatus(final FightObject fightObject, final List<Integer> statusList) {
        int returnStatus = 0;
        for (final Integer status : statusList) {
            if (fightObject.getStatusValue(status) > 0) {
                returnStatus = status;
                break;
            }
        }
        return returnStatus;
    }
}
