
package com.kitty.system.game.fightnew.reward;

import com.kitty.system.game.pet.service.PetService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.bean.Fight;
import org.springframework.stereotype.Component;

@Component
public class FightRewardIncenseNewHandler extends FightRewardHandler
{
    @Override
    public void sendReward(final Fight fight, final Role role, final Pet pet) {
        final int roleExp = (int)(5000000.0 * (1.0 + (role.getLevel() + role.getRelive() * 15) / 140 * 2.5) / 30.0 / 28.13 * 0.3 * 1.5);
        SpringUtils.getRoleService().addExp(role, roleExp, null, true);
        if (pet != null) {
            final int petExp = (int)(5000000.0 * ((1.0 + (pet.getPetLv() + pet.getRebirth() * 15) / 140 * 2.5) / 30.0 / 28.13 * 0.3 * 1.5) * 1.2);
            SpringUtils.getBean(PetService.class).addExp(role, pet, petExp, true);
        }
    }
    
    @Override
    public void fightFailed(final Fight fight, final Role role) {
    }
    
    @Override
    public void flee(final Fight fight, final Role role) {
    }
    
    @Override
    public int getType() {
        return 199;
    }
    
    @Override
    public void init() {
        super.putHndlers(this);
    }
}
