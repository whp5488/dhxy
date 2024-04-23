package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
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

public class SKILL_80023 extends SkillProcessor {
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
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 80023);
    SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(80023));
    if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, target, false, args))
      return; 
    if (args.length < 2)
      return; 
    Integer skillSerial = (Integer)args[1];
    if (skillSerial != null && 
      PetSkillEffectorUtil.isClash(petSkill, skillSerial))
      return; 
    double percent = 0.15D;
    double pval = AcsryPetSkillUtil.getPval(80023, effector);
    if (pval > 0.0D)
      percent += pval; 
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      if (effector.getSkillCoolMap().get(String.valueOf(80023)) != null && ((Integer)effector.getSkillCoolMap().get(String.valueOf(80023))).intValue() >= 1)
        return; 
      if (effector.getSkillCoolMap().get(String.valueOf(81002)) != null && ((Integer)effector.getSkillCoolMap().get(String.valueOf(81002))).intValue() >= 1)
        return; 
      FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId().intValue(), target.getId().intValue());
      if (newTarget == null)
        return; 
      effector.getSkillCoolMap().setv(String.valueOf(80023), Integer.valueOf(1));
      RoleSkill skillBy = effector.getSkillBy(Integer.valueOf(80023));
      BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
      battleUnit.countDoubleHit();
      int doubleHit = 1;
      if (SkillAi.checkDoubleHit(effector)) {
        int num = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), effector.getPropertyMap().get(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())));
        doubleHit += num;
      } 
      boolean flagZ = SkillAi.checkPetIsSkill(fight, effector.isASide(), Integer.valueOf(81057));
      if (flagZ) {
        int num = PetPassiveSkillUtil.reduceDoubleHit(fight, effector);
        doubleHit = Math.max(1, doubleHit - num);
      } 
      battleUnit.setDoubleHit(doubleHit);
      PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 80023);
      skillRecord.setT(newTarget.getId().intValue());
    } 
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
