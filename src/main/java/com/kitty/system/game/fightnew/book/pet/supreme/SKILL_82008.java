package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_82008 extends SkillProcessor {
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
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 82008);
    Effect effect = new Effect(99, 0);
    if (!effector.getId().equals(target.getId()) && effector.getSkillCoolMap().getInt(String.valueOf(82008)) > 0)
      return null; 
    BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(target.isASide()));
    if (!beforeActionSkill.getSkills().contains(Integer.valueOf(82008)))
      return null; 
    Integer skillid = beforeActionSkill.getSkills().stream().filter(skill -> (82008 == skill.intValue())).findAny().get();
    if (skillid != null && skillid.intValue() > 0) {
      effector.getSkillCoolMap().setv(String.valueOf(82008), Integer.valueOf(fight.getMaxRound()));
      ActionParamUtils.actionForTargetNoth(fight, effector, target, 7, 82008, 2);
      PetSkillEffectorUtil.enemyFirstMagicCancelAction(fight, effector, target, beforeActionSkill, skillid);
      skillRecord.setT(target.getId().intValue());
      return effect;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
