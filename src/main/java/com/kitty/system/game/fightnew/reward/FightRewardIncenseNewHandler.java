package com.kitty.system.game.fightnew.reward;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class FightRewardIncenseNewHandler extends FightRewardHandler {
  public void sendReward(Fight fight, Role role, Pet pet) {
    int roleExp = (int)(5000000.0D * (1.0D + ((role.getLevel().intValue() + role.getRelive().intValue() * 15) / 140) * 2.5D) / 30.0D / 28.13D * 0.3D * 1.5D);
    SpringUtils.getRoleService().addExp(role, roleExp, null, true);
    if (pet != null) {
      int petExp = (int)(5000000.0D * (1.0D + ((pet.getPetLv().intValue() + pet.getRebirth().intValue() * 15) / 140) * 2.5D) / 30.0D / 28.13D * 0.3D * 1.5D * 1.2D);
      ((PetService)SpringUtils.getBean(PetService.class)).addExp(role, pet, petExp, true);
    } 
  }
  
  public void fightFailed(Fight fight, Role role) {}
  
  public void flee(Fight fight, Role role) {}
  
  public int getType() {
    return 199;
  }
  
  public void init() {
    putHndlers(this);
  }
}
