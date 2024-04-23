package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.Arrays;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_80020 extends SkillProcessor {
  static final List<String> terms = Arrays.asList(new String[0]);
  
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
    if (actionDetailParam == null)
      return; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 80020);
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 3.0D * (effector.getRelive() * 0.4D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      int oldHurt = ((Integer)args[0]).intValue();
      RoleSkill skill = effector.getSkillBy(Integer.valueOf(80020));
      SysPetSkill sysSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(80020));
      int proficient = 1;
      if (skill.getProficient() != null)
        proficient = skill.getProficient().intValue(); 
      double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
      double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_SS.getDist());
      double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_SS.getDist());
      double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SS.getDist());
      double hurt = sysSkill.getPower()[0] * effector.getLevel() * (Math.pow(proficient, 0.4D) * 2.8853998118144273D / 100.0D + 1.0D);
      double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
      int harm = SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx).intValue();
      oldHurt += harm;
      int reduceLife = oldHurt;
      if (reduceLife > target.getCurrLife())
        reduceLife = target.getCurrLife(); 
      FightObjectParam targetReduce = ActionParamUtils.changeLifeForTarget(fight, target, 1, -reduceLife, 1);
      targetReduce.addDamageType(115);
      actionDetailParam.getTargetParamList().add(targetReduce);
      FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(1), terms);
      int life = Math.min(unit.getCurrLife() + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(oldHurt * 0.03D)), unit.getMaxLife());
      int addLife = life - unit.getCurrLife();
      if (addLife > 0) {
        FightObjectParam targetAdd = ActionParamUtils.changeLifeForTarget(fight, unit, 1, addLife, 1);
        actionDetailParam.getTargetParamList().add(targetAdd);
      } 
      skillRecord.setT(unit.getId().intValue());
    } 
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
