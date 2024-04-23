package com.kitty.system.game.fightnew.book.pet.senior;

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
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_81002 extends SkillProcessor {
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
    if (target == null || !target.isFallDown())
      return; 
    FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), true);
    if (!targetParam.isHit())
      return; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81002);
    int num = effector.getSkillCoolMap().getInt("81002value");
    if (num < 2) {
      double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 7.0D * (effector.getRelive() * 0.6D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
      if (num >= 0) {
        percent = 1.0D;
      } else {
        double pval = AcsryPetSkillUtil.getPval(81002, effector);
        percent += pval;
      } 
      boolean flag = SkillAi.canAffectOnPro(percent);
      if (flag) {
        if (effector.getSkillCoolMap().getInt(String.valueOf(81002)) > 0 && effector.getSkillCoolMap().getInt("81002value") >= 2)
          return; 
        effector.getSkillCoolMap().setv(String.valueOf(81002), Integer.valueOf(1));
        num++;
        effector.getSkillCoolMap().setv("81002value", Integer.valueOf(num));
        FightObject newTarget = fight.getTargetNoCurrentTarget(effector.getId().intValue(), target.getId().intValue());
        if (newTarget == null)
          return; 
        RoleSkill skillBy = effector.getSkillBy(Integer.valueOf(81002));
        BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
        battleUnit.countDoubleHit();
        int doubleHit = 1;
        if (SkillAi.checkDoubleHit(effector)) {
          int numZ = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), effector.getPropertyMap().get(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())));
          doubleHit += numZ;
        } 
        boolean flagZ = SkillAi.checkPetIsSkill(fight, effector.isASide(), Integer.valueOf(81057));
        if (flagZ) {
          int reduceNum = PetPassiveSkillUtil.reduceDoubleHit(fight, effector);
          doubleHit = Math.max(1, doubleHit - reduceNum);
        } 
        battleUnit.setDoubleHit(doubleHit);
        PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 81002);
        skillRecord.setT(newTarget.getId().intValue());
        if (newTarget.isFallDown())
          normalAttack(fight, effector, newTarget, actionDetailParam, new Object[0]); 
      } 
    } 
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
