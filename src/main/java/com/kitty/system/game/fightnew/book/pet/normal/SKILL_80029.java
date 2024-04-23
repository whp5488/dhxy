package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.Arrays;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_80029 extends SkillProcessor {
  static final List<String> terms = Arrays.asList(new String[] { PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue() });
  
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 80029);
    SysPetSkill petSkill = (SysPetSkill)args[1];
    if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args))
      return null; 
    int count = 1;
    boolean trigger = AcsryPetSkillUtil.isTrigger(80029, effector);
    if (trigger)
      count++; 
    for (int i = 0; i < count; i++) {
      FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(1), terms);
      if (unit != null) {
        excute(fight, effector, unit);
        skillRecord.setT(unit.getId().intValue());
      } 
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
  
  private void excute(Fight fight, FightObject effector, FightObject unit) {
    if (unit != null) {
      int mana = Math.min(unit.getCurrMana() + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(unit.getMaxMana() * 0.1D)), unit.getMaxMana());
      int life = Math.min(unit.getCurrLife() + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(unit.getMaxLife() * 0.7D)), unit.getMaxLife());
      int oldLife = unit.getCurrLife();
      int oldMana = unit.getCurrMana();
      ActionParamUtils.changeHpMp(fight, effector, unit, 7, 80029, 2, life - oldLife, mana - oldMana, false);
    } 
  }
}
