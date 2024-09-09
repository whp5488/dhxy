package com.kitty.system.game.instruct.handler;

import java.util.Map;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class ExpHandler implements NcFather
{
    public ExpHandler() {
    }
    
    @Override
    public void handle(Role role, String content) {
        if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
            return;
        }
        Long num = Long.valueOf(content);
        SpringUtils.getRoleService().addExp(role, (long)num, "", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            Map<Integer, Pet> map = SpringUtils.getPetService().selectPet(role);
            Pet pet = (Pet)map.get(combatPet);
            ((PetService)SpringUtils.getBean(PetService.class)).addExp(role, pet, (long)num, true);
        }
        return;
    }
}
