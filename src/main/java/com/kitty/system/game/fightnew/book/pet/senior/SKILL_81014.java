package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Arrays;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_81014 extends SkillProcessor {
  static final List<String> terms = Arrays.asList(new String[0]);
  
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81014);
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 2.0D * (effector.getRelive() * 0.4D + 1.0D) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(1), terms);
      if (!unit.isFallDown())
        return null; 
      int addLife = (int)(unit.getMaxLife() * 0.5D);
      ActionParamUtils.changeLife(fight, effector, unit, 7, 81014, 2, addLife, true);
      skillRecord.setT(unit.getId().intValue());
    } 
    return null;
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
