package com.kitty.system.game.pet.handler;

import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysPetSkill;
import java.text.MessageFormat;
import org.springframework.stereotype.Component;

@Component("DogzHandler")
public class DogzHandler extends FeedPetHandler {
  private static final String SUCCESS = "恭喜，你的召唤兽#<Y>{0}#学会了{1}";
  
  private static final Integer SILVER = Integer.valueOf(5000000);
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    if (sysPet == null)
      return null; 
    if (sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue() || sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
      Integer subType = SkillFunc.getPetSkillSubType(param);
      if (!subType.equals(Integer.valueOf(PETSKILLSUB.SUBTYPE_SPECIAL.getValue())))
        return null; 
      SysPetSkill skill = (SysPetSkill)SkillDataPool.petSkillMap.get(param);
      if (skill != null) {
        boolean flag = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, SILVER.intValue(), "DogzHandler");
        if (flag)
          pet.getSkills().forEach(e -> {
                if (e.getType() == PETSKILLSUB.SUBTYPE_SPECIAL.getValue()) {
                  e.setSerial(param.intValue());
                  return;
                } 
              }); 
        this.petService.getArenaScore(role.getId().longValue(), pet);
        this.petService.loadPet(role, pet);
        pet.save();
        return MessageFormat.format("恭喜，你的召唤兽#<Y>{0}#学会了{1}", new Object[] { pet.getPetName(), skill.getName() });
      } 
    } 
    return null;
  }
}
