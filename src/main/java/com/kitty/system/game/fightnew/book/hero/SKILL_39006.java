package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_39006 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    List<FightObject> targetSide = fight.getTargetSide(effector.getId().intValue());
    if (targetSide.size() > 0) {
      targetSide = (List<FightObject>)targetSide.stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
      if (targetSide.size() > 0)
        return targetSide.size(); 
    } 
    return 0;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    int currLife = target.getCurrLife();
    Effect e = new Effect();
    e.setType(100000);
    e.setHurt(currLife);
    return e;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
