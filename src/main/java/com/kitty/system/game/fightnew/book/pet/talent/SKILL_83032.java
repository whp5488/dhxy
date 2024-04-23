package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_83032 extends SkillProcessor {
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
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83032);
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 13.0D * (effector.getRelive() * 0.5D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag)
      if (PetPassiveSkillUtil.isAbnormalOrPositiveStatus(target.getStatusMap(), "positive")) {
        List<FiedValue> list = PetPassiveSkillUtil.clearStatus(target, "positive");
        Effect effect = new Effect();
        effect.setList(list);
        skillRecord.setT(target.getId().intValue());
        return effect;
      }  
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
