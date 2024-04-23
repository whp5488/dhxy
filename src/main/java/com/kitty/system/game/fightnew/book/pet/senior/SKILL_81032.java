package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_81032 extends SkillProcessor {
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
    return null;
  }
  
  public void normalAttack(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81032);
    SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(81032));
    if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, target, false, args))
      return; 
    if (args.length < 2)
      return; 
    Integer skillSerial = (Integer)args[1];
    if (skillSerial != null && 
      PetSkillEffectorUtil.isClash(petSkill, skillSerial))
      return; 
    double percent = 0.35D;
    double pval = AcsryPetSkillUtil.getPval(80011, effector);
    if (pval > 0.0D)
      percent += pval; 
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      Object oldHurt = args[0];
      Integer hurt = Integer.valueOf(Integer.parseInt(oldHurt.toString()));
      Effect effect = new Effect();
      effect.setHurt(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(hurt.intValue() * 0.5D)));
      FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId().intValue(), target.getId().intValue());
      if (newTarget == null)
        return; 
      RoleSkill skillBy = effector.getSkillBy(Integer.valueOf(81032));
      BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
      battleUnit.setEffect(effect);
      PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 81032);
      skillRecord.setT(newTarget.getId().intValue());
    } 
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
