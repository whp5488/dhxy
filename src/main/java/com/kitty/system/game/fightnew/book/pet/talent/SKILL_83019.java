package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_83019 extends SkillProcessor {
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
    if (args.length > 0 && args[0] instanceof Integer) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83019);
      int hurt = ((Integer)args[0]).intValue();
      double percenta = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((16.0D + 5.2D * (effector.getRelive() * 0.4D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
      double percentb = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((8.0D + 2.6D * (effector.getRelive() * 0.4D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
      double percentc = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((4.0D + 1.3D * (effector.getRelive() * 0.4D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
      double percentd = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((28.0D + 9.1D * (effector.getRelive() * 0.4D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
      if (SkillAi.canAffectOnPro(percenta)) {
        hurt = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(hurt), String.valueOf(1.5D)));
      } else if (SkillAi.canAffectOnPro(percentb)) {
        hurt = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(hurt), String.valueOf(2)));
      } else if (SkillAi.canAffectOnPro(percentc)) {
        hurt = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(hurt), String.valueOf(2.5D)));
      } else if (SkillAi.canAffectOnPro(percentd)) {
        hurt = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(hurt), String.valueOf(0.5D)));
      } 
      skillRecord.setT(target.getId().intValue());
      return new Effect(0, hurt);
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
