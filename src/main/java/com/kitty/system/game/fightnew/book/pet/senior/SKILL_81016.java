package com.kitty.system.game.fightnew.book.pet.senior;

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

public class SKILL_81016 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 2.7D * (addition.getRei() * 0.5D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(addition.getLv()), Integer.valueOf(addition.getRei()), addition.getExperience())) / 100.0D));
    NutMap map = NutMap.NEW();
    map.setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(percent));
    map.setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(percent));
    map.setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(percent));
    return map;
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
