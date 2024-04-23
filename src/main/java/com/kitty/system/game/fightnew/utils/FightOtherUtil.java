package com.kitty.system.game.fightnew.utils;

import com.kitty.system.enums.war.wardef.LOGIC_SKILL;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FightOtherUtil {
  public static List<Integer> addSkillIds(Pet pet) {
    if (pet.getSkills() != null && pet.getSkills().size() >= 0) {
      List<Integer> skills = new ArrayList<>();
      for (PetSkill petSkill : pet.getSkills())
        skills.add(Integer.valueOf(petSkill.getSerial())); 
      if (skills.size() > 0)
        return skills; 
    } 
    return null;
  }
  
  public static int isPlayerSkill(int skillSerial) {
    int returnValue = 0;
    if (skillSerial <= 0)
      return returnValue; 
    int length = (int)(Math.log(skillSerial) / Math.log(10.0D) + 1.0D);
    if (length == 5) {
      int index = skillSerial / 10000;
      if (index == 3) {
        returnValue = LOGIC_SKILL.LOGIC_TYPE_SKILL.getValue();
      } else if (index == 8) {
        returnValue = LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue();
      } 
    } else if (length == 6) {
      returnValue = LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue();
    } 
    return returnValue;
  }
  
  public static boolean isHaveStatus(FightObject fightObject, List<Integer> statusList) {
    Set<String> statusStr = fightObject.getStatusMap().keySet();
    for (Integer status : statusList) {
      if (statusStr.contains(String.valueOf(status)))
        return true; 
    } 
    return false;
  }
}
