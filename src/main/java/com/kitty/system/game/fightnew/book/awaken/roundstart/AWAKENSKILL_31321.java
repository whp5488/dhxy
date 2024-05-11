
package com.kitty.system.game.fightnew.book.awaken.roundstart;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import java.util.Arrays;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31321 extends AwakenSkillProcessor
{
    @Override
    public void roundStart(final Fight fight, final PetFightObject petFightObject, final Object... args) {
        final List<Integer> icons = Arrays.asList(6048, 6049, 6047, 6051);
        final List<FightObject> mySide = fight.getMySide(petFightObject.getId());
        boolean flag = false;
        for (final FightObject fightObject : mySide) {
            if (icons.contains(fightObject.getIcon())) {
                flag = true;
                break;
            }
        }
        if (flag) {
            final Role role = petFightObject.getRole();
            final Pet pet = petFightObject.getPet();
            final byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
            if (suitLevParam > 0) {
                double percent = this.getPercent(suitLevParam);
                if (pet.getAwaken() > 0) {
                    percent += this.getPercent(pet.getAwaken());
                }
                ImbueUtil.addRound(petFightObject.getImbueMap(), PRO_DICT_TYPE.K_F.getDist(), 1, percent);
                ImbueUtil.addRound(petFightObject.getImbueMap(), PRO_DICT_TYPE.K_L.getDist(), 1, percent);
                ImbueUtil.addRound(petFightObject.getImbueMap(), PRO_DICT_TYPE.K_S.getDist(), 1, percent);
                ImbueUtil.addRound(petFightObject.getImbueMap(), PRO_DICT_TYPE.K_H.getDist(), 1, percent);
            }
        }
    }
    
    private double getPercent(final short param) {
        return 20 * param / 20.0 / 100.0;
    }
}
