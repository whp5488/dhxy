package com.kitty.system.game.fightnew.book.awaken.roundstart;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import java.util.stream.Collectors;

public class AWAKENSKILL_31517 extends AwakenSkillProcessor {
  public void roundStart(Fight fight, PetFightObject petFightObject, Object... args) {
    if (getFlag(fight, petFightObject)) {
      Role role = petFightObject.getRole();
      Pet pet = petFightObject.getPet();
      byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
      if (suitLevParam > 0) {
        double percent = getPercent((short)suitLevParam);
        if (pet.getAwaken() > 0)
          percent += getPercent(pet.getAwaken()); 
        int round = 1;
        ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), round, percent);
        petFightObject.setStatus(2189, round);
        ActionParamUtils.addStatus(fight, (FightObject)petFightObject, (FightObject)petFightObject, 43, 31517, 2, 2189, round);
      } 
    } 
  }
  
  private boolean getFlag(Fight fight, PetFightObject petFightObject) {
    int pos = petFightObject.getPos();
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(petFightObject.getId().intValue()).stream().filter(obj -> (obj.getPos() == pos - 100 || obj.getPos() == pos + 1 || obj.getPos() == pos - 1)).collect(Collectors.toList());
    if (mySide != null && mySide.size() >= 3) {
      int black = 0;
      int white = 0;
      for (FightObject fightObject : mySide) {
        ImbueParam imbueParam = ImbueUtil.gainProForSkill(fightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist()), 83040 + petFightObject
            .getId().intValue(), false);
        if (imbueParam != null)
          black++; 
        imbueParam = ImbueUtil.gainProForSkill(fightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), 83040 + petFightObject
            .getId().intValue(), false);
        if (imbueParam != null)
          white++; 
      } 
      if (black == 3 || white == 3)
        return true; 
    } 
    return false;
  }
  
  private double getPercent(short param) {
    return (40 * param) / 20.0D / 100.0D;
  }
}
