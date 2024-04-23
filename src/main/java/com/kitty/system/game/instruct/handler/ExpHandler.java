package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import java.util.Map;

public class ExpHandler implements NcFather {
  public void handle(Role role, String content) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
      return; 
    Long num = Long.valueOf(content);
    SpringUtils.getRoleService().addExp(role, num.longValue(), "", true);
    Integer combatPet = role.getExtendBox().getCombatPet();
    if (combatPet.intValue() > 0) {
      Map<Integer, Pet> map = SpringUtils.getPetService().selectPet(role);
      Pet pet = map.get(combatPet);
      ((PetService)SpringUtils.getBean(PetService.class)).addExp(role, pet, num.longValue(), true);
    } 
  }
}
