
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
    @Override
    public void handle(final Role role, final String content) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        final Long num = Long.valueOf(content);
        SpringUtils.getRoleService().addExp(role, num, "", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final Map<Integer, Pet> map = SpringUtils.getPetService().selectPet(role);
            final Pet pet = map.get(combatPet);
            SpringUtils.getBean(PetService.class).addExp(role, pet, num, true);
        }
    }
}
