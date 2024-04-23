package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_83039 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public void normalAttack(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (target.isFallDown() || !effector.isPet())
      return; 
    FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), false);
    if (targetParam == null || !targetParam.isHit())
      return; 
    int skillCool = effector.getSkillCoolMap().getInt(String.valueOf(83039));
    if (skillCool > 0)
      return; 
    boolean flag = SkillAi.canAffectOnPro(getPercent(effector));
    if (flag) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83039);
      effector.getSkillCoolMap().setv(String.valueOf(83039), Integer.valueOf(0));
      effector.setType(0);
      effector.setSkillId(0);
      RoleSkill skill = new RoleSkill();
      skill.setType(Integer.valueOf(0));
      skill.setSkillId(Integer.valueOf(83039));
      AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31427, target);
      BattleUnit battleUnit = new BattleUnit(fight, effector, target, skill);
      battleUnit.countDoubleHit();
      PetPassiveSkillUtil.passivePhysics(fight, effector, target, battleUnit, actionDetailParam, 83039);
      skillRecord.setT(target.getId().intValue());
    } 
  }
  
  private double getPercent(FightObject effector) {
    double param1 = 4.0D * (effector.getRelive() * 0.8D + 1.0D);
    double param2 = Math.pow(effector.getLevel(), 0.5D) / 10.0D + Math.pow(effector.getClose(), 0.16666666666666666D) * 10.0D / (100 + effector.getRelive() * 20);
    return Math.min(50.0D, 5.0D + param1 * param2) / 100.0D;
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
