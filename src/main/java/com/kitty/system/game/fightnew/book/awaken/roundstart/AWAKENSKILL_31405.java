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
    public AWAKENSKILL_31405() {
    }
    
    @Override
    public void roundStart(Fight fight, PetFightObject petFightObject, Object... args) {
        FightObject host = fight.queryFightObject(Integer.valueOf(petFightObject.getHostId()));
        if (!host.isFallDown()) {
            String lifePct = BigDecimalUtil.divide(String.valueOf(host.getCurrLife()), String.valueOf(host.getMaxLife()), 3);
            if (Double.parseDouble(lifePct) < 0.4) {
                Role role = petFightObject.getRole();
                Pet pet = petFightObject.getPet();
                byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
                if (suitLevParam > 0) {
                    double percent = this.getPercent((short)suitLevParam);
                    if (pet.getAwaken() > 0) {
                        percent += this.getPercent(pet.getAwaken());
                    }
                    boolean flag = SkillAi.canAffectOnPro(percent);
                    if (flag) {
                        int addLife = (int)((double)host.getMaxLife() * 0.5);
                        ActionParamUtils.changeLife(fight, petFightObject, host, 7, 31405, 2, addLife, false);
                    }
                }
            }
        }
    }
    
    private double getPercent(short param) {
        return (double)(30 * param) / 20.0 / 100.0;
    }
}
