
package com.kitty.system.game.fightnew.book.awaken.roundstart;

import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31517 extends AwakenSkillProcessor
{
    @Override
    public void roundStart(final Fight fight, final PetFightObject petFightObject, final Object... args) {
        if (this.getFlag(fight, petFightObject)) {
            final Role role = petFightObject.getRole();
            final Pet pet = petFightObject.getPet();
            final byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
            if (suitLevParam > 0) {
                double percent = this.getPercent(suitLevParam);
                if (pet.getAwaken() > 0) {
                    percent += this.getPercent(pet.getAwaken());
                }
                final int round = 1;
                ImbueUtil.addRound(petFightObject.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), round, percent);
                petFightObject.setStatus(2189, round);
                ActionParamUtils.addStatus(fight, petFightObject, petFightObject, 43, 31517, 2, 2189, round);
            }
        }
    }
    
    private boolean getFlag(final Fight fight, final PetFightObject petFightObject) {
        final int pos = petFightObject.getPos();
        final List<FightObject> mySide = fight.getMySide(petFightObject.getId()).stream().filter(obj -> obj.getPos() == pos - 100 || obj.getPos() == pos + 1 || obj.getPos() == pos - 1).collect(Collectors.toList());
        if (mySide != null && mySide.size() >= 3) {
            int black = 0;
            int white = 0;
            for (final FightObject fightObject : mySide) {
                ImbueParam imbueParam = ImbueUtil.gainProForSkill(fightObject.getImbueMap(), PRO_DICT_TYPE.BURN_HURT.getDist(), 83040 + petFightObject.getId(), false);
                if (imbueParam != null) {
                    ++black;
                }
                imbueParam = ImbueUtil.gainProForSkill(fightObject.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), 83040 + petFightObject.getId(), false);
                if (imbueParam != null) {
                    ++white;
                }
            }
            if (black == 3 || white == 3) {
                return true;
            }
        }
        return false;
    }
    
    private double getPercent(final short param) {
        return 40 * param / 20.0 / 100.0;
    }
}
