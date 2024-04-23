package com.kitty.system.game.pet.handler;

import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysPetSkill;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("SoulHandler")
public class SoulHandler extends FeedPetHandler {
  private static final String SUCCESS = "恭喜，你的召唤兽#<Y>{0}#学会了{1}";
  
  private static final Integer SILVER = Integer.valueOf(3000000);
  
  private static final List<Integer> SOUL = Arrays.asList(new Integer[] { Integer.valueOf(81055), Integer.valueOf(81007), Integer.valueOf(81006) });
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    if (sysPet == null)
      return null; 
    if (sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() || sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
      if (!SOUL.contains(param))
        return null; 
      List<Integer> unset = new ArrayList<>();
      for (int i = 0; i < pet.getSkills().size(); i++) {
        PetSkill petSkill = pet.getSkills().get(i);
        if (petSkill.getSerial() >= 0)
          unset.add(Integer.valueOf(i)); 
      } 
      if (unset.size() < 5)
        return "召唤兽解封第5技能格才可以学习哦"; 
      SysPetSkill skill = (SysPetSkill)SkillDataPool.petSkillMap.get(param);
      if (skill != null) {
        boolean flag = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, SILVER.intValue(), "SoulHandler");
        if (flag)
          pet.getSkills().forEach(e -> {
                if (e.getType() == PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
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
