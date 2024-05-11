
package com.kitty.system.game.pet.handler;

import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.text.MessageFormat;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("DogzHandler")
public class DogzHandler extends FeedPetHandler
{
    private static final String SUCCESS = "恭喜，你的召唤兽#<Y>{0}#学会了{1}";
    private static final Integer SILVER;
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        if (sysPet == null) {
            return null;
        }
        if (sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue() || sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
            final Integer subType = SkillFunc.getPetSkillSubType(param);
            if (!subType.equals(PETSKILLSUB.SUBTYPE_SPECIAL.getValue())) {
                return null;
            }
            final SysPetSkill skill = SkillDataPool.petSkillMap.get(param);
            if (skill != null) {
                final boolean flag = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, DogzHandler.SILVER, "DogzHandler");
                if (flag) {
                    pet.getSkills().forEach(e -> {
                        if (e.getType() == PETSKILLSUB.SUBTYPE_SPECIAL.getValue()) {
                            e.setSerial(param);
                            return;
                        }
                        else {
                            return;
                        }
                    });
                }
                this.petService.getArenaScore(role.getId(), pet);
                this.petService.loadPet(role, pet);
                pet.save();
                return MessageFormat.format("恭喜，你的召唤兽#<Y>{0}#学会了{1}", pet.getPetName(), skill.getName());
            }
        }
        return null;
    }
    
    static {
        SILVER = 5000000;
    }
}
