package com.kitty.system.game.fightnew.book.awaken.addition;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31310 extends AwakenSkillProcessor {
  public NutMap additionSuit(Role role, Pet pet, Object... args) {
    byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
    if (suitLevParam > 0) {
      double percent = getPercent((short)suitLevParam);
      NutMap map = NutMap.NEW();
      map.put(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), Double.valueOf(percent));
      return map;
    } 
    return null;
  }
  
  public NutMap additionAwaken(Role role, Pet pet, Object... args) {
    short awaken = pet.getAwaken();
    if (awaken > 0) {
      double percent = getPercent(awaken);
      NutMap map = NutMap.NEW();
      map.put(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), Double.valueOf(percent));
      return map;
    } 
    return null;
  }
  
  private double getPercent(short lev) {
    return (15 * lev) / 20.0D / 100.0D;
  }
}
