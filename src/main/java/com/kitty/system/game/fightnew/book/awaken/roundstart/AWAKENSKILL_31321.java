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
    public AWAKENSKILL_31321() {
    }
    
    @Override
    public void roundStart(Fight fight, PetFightObject petFightObject, Object... args) {
        List<Integer> icons = Arrays.asList(Integer.valueOf(6048), Integer.valueOf(6049), Integer.valueOf(6047), Integer.valueOf(6051));
        List<FightObject> mySide = fight.getMySide((int)petFightObject.getId());
        boolean flag = false;
        for (FightObject fightObject : mySide) {
            if (icons.contains(Integer.valueOf(fightObject.getIcon()))) {
                flag = true;
                break;
            }
            continue;
        }
        if (flag) {
            Role role = petFightObject.getRole();
            Pet pet = petFightObject.getPet();
            byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
            if (suitLevParam > 0) {
                double percent = this.getPercent((short)suitLevParam);
                if (pet.getAwaken() > 0) {
                    percent += this.getPercent(pet.getAwaken());
                }
                ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_F.getDist()), 1, percent);
                ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_L.getDist()), 1, percent);
                ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_S.getDist()), 1, percent);
                ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_H.getDist()), 1, percent);
            }
        }
    }
    
    private double getPercent(short param) {
        return (double)(20 * param) / 20.0 / 100.0;
    }
}
