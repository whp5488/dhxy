package com.kitty.system.game.fightnew.book.awaken.roundstart;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;

public class AWAKENSKILL_31405 extends AwakenSkillProcessor {
  public void roundStart(Fight fight, PetFightObject petFightObject, Object... args) {
    FightObject host = fight.queryFightObject(Integer.valueOf(petFightObject.getHostId()));
    if (!host.isFallDown()) {
      String lifePct = BigDecimalUtil.divide(String.valueOf(host.getCurrLife()), String.valueOf(host.getMaxLife()), 3);
      if (Double.parseDouble(lifePct) < 0.4D) {
        Role role = petFightObject.getRole();
        Pet pet = petFightObject.getPet();
        byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
        if (suitLevParam > 0) {
          double percent = getPercent((short)suitLevParam);
          if (pet.getAwaken() > 0)
            percent += getPercent(pet.getAwaken()); 
          boolean flag = SkillAi.canAffectOnPro(percent);
          if (flag) {
            int addLife = (int)(host.getMaxLife() * 0.5D);
            ActionParamUtils.changeLife(fight, (FightObject)petFightObject, host, 7, 31405, 2, addLife, false);
          } 
        } 
      } 
    } 
  }
  
  private double getPercent(short param) {
    return (30 * param) / 20.0D / 100.0D;
  }
}
