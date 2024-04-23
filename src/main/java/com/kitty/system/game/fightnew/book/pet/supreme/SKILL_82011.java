package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_82011 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, target.getId().intValue(), 82011);
    int hurt = ((Integer)args[0]).intValue();
    int oldCurrMana = target.getCurrMana();
    int surplusMana = oldCurrMana - hurt;
    int changeMana = -hurt;
    if (surplusMana <= 0)
      changeMana = -target.getCurrMana(); 
    ActionParamUtils.changeMana(fight, target, target, 7, 82011, 2, changeMana);
    skillRecord.setT(target.getId().intValue());
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
