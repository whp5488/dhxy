package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_83007 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    double percent = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(2), Double.valueOf((0.1D + 4.0D * (addition.getRei() * 0.4D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(addition.getLv()), Integer.valueOf(addition.getRei()), addition.getExperience())) / 100.0D));
    double hitRatio = addition.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()));
    if (hitRatio > 0.0D) {
      hitRatio += percent;
    } else {
      hitRatio = percent;
    } 
    addition.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), Double.valueOf(hitRatio));
    double rageRatio = addition.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()));
    if (rageRatio > 0.0D) {
      rageRatio += percent;
    } else {
      rageRatio = percent;
    } 
    addition.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), Double.valueOf(rageRatio));
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
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
