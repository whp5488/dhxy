package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_81005 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    if (effector.isFallDown())
      return null; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81005);
    double addK = 0.01D;
    double maxK = 0.1D;
    double k_hl = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
    double k_hs = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
    double k_fy = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
    boolean isAddHl = PetSkillEffectorUtil.addAttrObj(fight, effector, addK, maxK, Integer.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
    if (isAddHl)
      effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(k_hl + addK)); 
    boolean isAddHs = PetSkillEffectorUtil.addAttrObj(fight, effector, addK, maxK, Integer.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
    if (isAddHs) {
      effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(k_hs + addK));
      skillRecord.setT(effector.getId().intValue());
    } 
    boolean isAddFy = PetSkillEffectorUtil.addAttrObj(fight, effector, addK, maxK, Integer.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
    if (isAddFy) {
      effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(k_fy + addK));
      skillRecord.setT(effector.getId().intValue());
    } 
    if (!isAddHl || !isAddHs || !isAddFy)
      return null; 
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
