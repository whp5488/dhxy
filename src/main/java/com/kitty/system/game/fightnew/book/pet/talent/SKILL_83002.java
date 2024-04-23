package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_83002 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    NutMap map = NutMap.NEW();
    double percent = 0.15D;
    map.setv(String.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist()), Double.valueOf(percent));
    map.setv(String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist()), Double.valueOf(percent));
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
