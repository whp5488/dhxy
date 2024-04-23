package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
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

public class SKILL_80010 extends SkillProcessor {
  static final List<String> terms = Arrays.asList(new String[] { PET_SKILL_TERM_DICT.PRO_BASS_PET_N.getValue(), PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue() });
  
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 80010);
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 2.0D * (effector.getRelive() * 0.4D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(2), terms, false);
      if (unit != null) {
        double probability = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), 
            Double.valueOf(unit.getCurrMana() / unit.getMaxMana()));
        if (probability >= 0.3D)
          return null; 
        int addMana = (int)(unit.getMaxMana() * 0.5D);
        if (addMana + unit.getCurrMana() > unit.getMaxMana())
          addMana = unit.getMaxMana() - unit.getCurrMana(); 
        ActionParamUtils.changeMana(fight, effector, unit, 7, 80010, 2, addMana);
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
}
