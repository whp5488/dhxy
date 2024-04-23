package com.kitty.system.game.role.utils;

import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import com.kitty.system.game.role.domain.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoleUtils implements Serializable {
  public static void removeAssistList(Role role, Pet pet, Integer oldSkillSerial) {
    boolean isRemove = true;
    for (PetSkill obj : pet.getSkills()) {
      if (obj.getSerial() != oldSkillSerial.intValue() && SkillConst.assistSkills.contains(Integer.valueOf(obj.getSerial()))) {
        isRemove = false;
        break;
      } 
    } 
    if (isRemove)
      updateAssistList(role, pet.getPetId(), false); 
  }
  
  public static void updateAssistList(Role role, Integer petId, boolean isAdd) {
    List<SuitParam> suits = role.getExtendBox().getSuits();
    if (isAdd) {
      if (suits.size() <= 0) {
        List<Integer> petIds = new ArrayList<>();
        petIds.add(petId);
        SuitParam suit = new SuitParam();
        suit.setSerial(Integer.valueOf(suits.size() + 1));
        suit.setPetIds(petIds);
        suits.add(suit);
      } else {
        for (SuitParam suit : suits) {
          if (suit.getPetIds().contains(petId))
            continue; 
          suit.getPetIds().add(petId);
        } 
      } 
    } else {
      if (suits.size() <= 0)
        return; 
      List<SuitParam> removeList = new ArrayList<>();
      for (SuitParam suit : suits) {
        suit.getPetIds().remove(petId);
        if (suit.getPetIds() == null || suit.getPetIds().size() <= 0)
          removeList.add(suit); 
      } 
      if (removeList.size() > 0)
        for (SuitParam suit : removeList)
          suits.remove(suit);  
    } 
  }
  
  public static void toUpdateAssistList(Role role, Pet pet, boolean isAdd) {
    List<PetSkill> skills = pet.getSkills();
    if (skills.size() > 0)
      for (PetSkill petSkill : skills) {
        if (SkillConst.assistSkills.contains(Integer.valueOf(petSkill.getSerial())))
          updateAssistList(role, pet.getPetId(), isAdd); 
      }  
  }
}
