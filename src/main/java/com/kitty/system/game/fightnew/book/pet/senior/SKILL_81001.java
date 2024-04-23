package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_81001 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    if (effector.getCurrLife() < effector.getMaxLife() * 0.4D) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81001);
      double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 1.5D * (effector.getRelive() * 0.5D + 1.0D) * (Math.pow(effector.getLevel(), 0.5D) / 10.0D + 
            Math.pow(effector.getClose(), 0.16666666666666666D) * 10.0D / (100 + effector.getRelive() * 20))) / 100.0D));
      if (SkillAi.canAffectOnPro(percent)) {
        int round = 3;
        effector.getStatusMap().setv(String.valueOf(1011), Integer.valueOf(round));
        effector.getStatusMap().setv("1011value", Integer.valueOf(99));
        ActionParamUtils.addStatus(fight, effector, effector, 7, 81001, 2, 1011, round);
        skillRecord.setT(effector.getId().intValue());
      } 
    } 
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 0;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
