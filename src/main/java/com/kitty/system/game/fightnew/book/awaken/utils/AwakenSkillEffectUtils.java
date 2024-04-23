package com.kitty.system.game.fightnew.book.awaken.utils;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class AwakenSkillEffectUtils {
  public static NutMap getPercentDouble(PetFightObject petFightObject, double param1, double param2) {
    Role role = petFightObject.getRole();
    Pet pet = petFightObject.getPet();
    if (role == null || pet == null)
      return null; 
    byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
    if (suitLevParam > 0) {
      double percent = getPercent(suitLevParam, param1, param2);
      if (pet.getAwaken() > 0)
        percent += getPercent(pet.getAwaken(), param1, param2); 
      return NutMap.NEW().setv("percent", Double.valueOf(percent));
    } 
    return null;
  }
  
  private static double getPercent(int param, double param1, double param2) {
    return param1 * param / param2 / 100.0D;
  }
  
  public static int getStatus(FightObject fightObject, List<Integer> statusList) {
    int returnStatus = 0;
    for (Integer status : statusList) {
      if (fightObject.getStatusValue(status.intValue()) > 0) {
        returnStatus = status.intValue();
        break;
      } 
    } 
    return returnStatus;
  }
}
