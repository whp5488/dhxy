package com.kitty.system.game.fightnew.book.awaken.roundstart;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31306 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31306() {
    }
    
    @Override
    public void roundStart(Fight fight, PetFightObject petFightObject, Object... args) {
        int statusValue = petFightObject.getStatusValue(1010);
        if (statusValue > 0) {
            Role role = petFightObject.getRole();
            Pet pet = petFightObject.getPet();
            byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
            if (suitLevParam > 0) {
                double percent = this.getPercent((short)suitLevParam);
                if (pet.getAwaken() > 0) {
                    percent += this.getPercent(pet.getAwaken());
                }
                ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), 1, percent);
            }
        }
    }
    
    private double getPercent(short param) {
        return (double)(12 * param) / 20.0 / 100.0;
    }
}
