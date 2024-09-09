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
    public FightRewardIncenseNewHandler() {
    }
    
    @Override
    public void sendReward(Fight fight, Role role, Pet pet) {
        int roleExp = (int)(5000000.0 * (1.0 + (double)(((int)role.getLevel() + (int)role.getRelive() * 15) / 140) * 2.5) / 30.0 / 28.13 * 0.3 * 1.5);
        SpringUtils.getRoleService().addExp(role, (long)roleExp, null, true);
        if (pet != null) {
            int petExp = (int)(5000000.0 * ((1.0 + (double)(((int)pet.getPetLv() + (int)pet.getRebirth() * 15) / 140) * 2.5) / 30.0 / 28.13 * 0.3 * 1.5) * 1.2);
            ((PetService)SpringUtils.getBean(PetService.class)).addExp(role, pet, (long)petExp, true);
        }
    }
    
    @Override
    public void fightFailed(Fight fight, Role role) {
    }
    
    @Override
    public void flee(Fight fight, Role role) {
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
