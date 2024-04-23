package com.kitty.system.game.fightnew.book.awaken.roundstart;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.Arrays;
import java.util.List;

public class AWAKENSKILL_31321 extends AwakenSkillProcessor {
  public void roundStart(Fight fight, PetFightObject petFightObject, Object... args) {
    List<Integer> icons = Arrays.asList(new Integer[] { Integer.valueOf(6048), Integer.valueOf(6049), Integer.valueOf(6047), Integer.valueOf(6051) });
    List<FightObject> mySide = fight.getMySide(petFightObject.getId().intValue());
    boolean flag = false;
    for (FightObject fightObject : mySide) {
      if (icons.contains(Integer.valueOf(fightObject.getIcon()))) {
        flag = true;
        break;
      } 
    } 
    if (flag) {
      Role role = petFightObject.getRole();
      Pet pet = petFightObject.getPet();
      byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
      if (suitLevParam > 0) {
        double percent = getPercent((short)suitLevParam);
        if (pet.getAwaken() > 0)
          percent += getPercent(pet.getAwaken()); 
        ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_F.getDist()), 1, percent);
        ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_L.getDist()), 1, percent);
        ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_S.getDist()), 1, percent);
        ImbueUtil.addRound(petFightObject.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_H.getDist()), 1, percent);
      } 
    } 
  }
  
  private double getPercent(short param) {
    return (20 * param) / 20.0D / 100.0D;
  }
}
