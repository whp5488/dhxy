package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31509 extends AwakenSkillProcessor {
  public NutMap additionSuit(Role role, Pet pet, Object... args) {
    byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
    if (suitLevParam > 0) {
      double percent = getPercent((short)suitLevParam);
      NutMap map = NutMap.NEW();
      map.put(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()), Double.valueOf(percent));
      return map;
    } 
    return null;
  }
  
  public NutMap additionAwaken(Role role, Pet pet, Object... args) {
    short awaken = pet.getAwaken();
    if (awaken > 0) {
      double percent = getPercent(awaken);
      NutMap map = NutMap.NEW();
      map.put(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()), Double.valueOf(percent));
      return map;
    } 
    return null;
  }
  
  public NutMap enteringSide(Fight fight, FightObject effector, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 100.0D, 20.0D);
    if (map != null) {
      double percent = map.getDouble("percent");
      if (percent > 0.0D && SkillAi.canAffectOnPro(percent)) {
        Pet pet = effector.getPet();
        if (pet != null) {
          int dict = getDict(pet);
          NutMap addMap = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 60.0D, 20.0D);
          double add = addMap.getDouble("percent");
          if (add > 0.0D) {
            ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(dict), 3, add * 100.0D);
            ActionParamUtils.actionForTargetNoth(fight, effector, effector, 7, 31509, 2);
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31509);
            skillRecord.setT(effector.getId().intValue());
          } 
        } 
      } 
    } 
    return null;
  }
  
  private int getDict(Pet pet) {
    int dict = 0;
    if (dict < pet.getMj().intValue())
      dict = PRO_DICT_TYPE.RISE_SPEED.getDist(); 
    if (dict < pet.getLl().intValue())
      dict = PRO_DICT_TYPE.RISE_ATTACK.getDist(); 
    if (dict < pet.getGg().intValue())
      dict = PRO_DICT_TYPE.RISE_DEF.getDist(); 
    if (dict < pet.getLx().intValue())
      dict = PRO_DICT_TYPE.RISE_DEF.getDist(); 
    return dict;
  }
  
  private double getPercent(short lev) {
    return (60 * lev) / 20.0D;
  }
}
