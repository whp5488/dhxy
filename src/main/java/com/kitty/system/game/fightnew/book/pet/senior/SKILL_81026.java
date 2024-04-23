package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.common.utils.BigDecimalUtil;
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
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_81026 extends SkillProcessor {
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
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81026);
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 4.0D * (effector.getRelive() * 0.4D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    int hurt = ((Integer)args[0]).intValue();
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      FightObject master = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
      if (master.isFallDown())
        return; 
      int addBlood = (int)((hurt * 0.3D >= 1.0D) ? (hurt * 0.3D) : 1.0D);
      int mana = Math.min(master.getCurrMana() + addBlood, master.getMaxMana());
      int addMana = mana - master.getCurrMana();
      FightObjectParam newTargetParam = ActionParamUtils.changeManaForTarget(fight, master, 1, addMana, 2);
      newTargetParam.addDamageType(2051);
      actionDetailParam.getTargetParamList().add(newTargetParam);
      skillRecord.setT(master.getId().intValue());
    } 
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
