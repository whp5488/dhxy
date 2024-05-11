
package com.kitty.system.game.fightnew.book.awaken.roundstart;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31405 extends AwakenSkillProcessor
{
    @Override
    public void roundStart(final Fight fight, final PetFightObject petFightObject, final Object... args) {
        final FightObject host = fight.queryFightObject(petFightObject.getHostId());
        if (!host.isFallDown()) {
            final String lifePct = BigDecimalUtil.divide(String.valueOf(host.getCurrLife()), String.valueOf(host.getMaxLife()), 3);
            if (Double.parseDouble(lifePct) < 0.4) {
                final Role role = petFightObject.getRole();
                final Pet pet = petFightObject.getPet();
                final byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
                if (suitLevParam > 0) {
                    double percent = this.getPercent(suitLevParam);
                    if (pet.getAwaken() > 0) {
                        percent += this.getPercent(pet.getAwaken());
                    }
                    final boolean flag = SkillAi.canAffectOnPro(percent);
                    if (flag) {
                        final int addLife = (int)(host.getMaxLife() * 0.5);
                        ActionParamUtils.changeLife(fight, petFightObject, host, 7, 31405, 2, addLife, false);
                    }
                }
            }
        }
    }
    
    private double getPercent(final short param) {
        return 30 * param / 20.0 / 100.0;
    }
}
