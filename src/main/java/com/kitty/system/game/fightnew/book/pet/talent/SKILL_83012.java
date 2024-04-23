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
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_83012 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    List<FightObject> targetSide = (List<FightObject>)fight.getTargetSide(effector.getId().intValue()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
    if (targetSide.size() < 2)
      return targetSide.size(); 
    return 2;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    Effect e = new Effect();
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(83012));
    if (skill != null) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83012);
      int level = effector.getLevel();
      if (level <= 0)
        level = 1; 
      double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((80 * level) + (effector.getMaxMana() / 100 * 6) * (effector.getRelive() * 0.6D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
      int harm = PetSkillEffectorUtil.thunderCount(fight, effector, target, hurt);
      e.setType(100000);
      e.setHurt(harm);
      skillRecord.setT(target.getId().intValue());
    } 
    return e;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    int level = PetSkillEffectorUtil.getExcuteLevel(args);
    return level * 32;
  }
}
