package com.kitty.system.game.fightnew.book.hero;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import java.util.Collections;
import org.nutz.lang.util.NutMap;

public class SKILL_30022 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30022));
    if (skill != null && 
      skill != null)
      return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(skill.getProficient().intValue(), 0.35D) * 5.0D / 100.0D)))); 
    return 3;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30022));
    if (skill != null) {
      Effect e = new Effect();
      int harmHp = 0;
      int harmMp = 0;
      if (!target.isFrozen()) {
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30022));
        int currLife = target.getCurrLife();
        int currMana = target.getCurrMana();
        double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
        int proficient = skill.getProficient().intValue();
        double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_ZS.getDist());
        double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_ZS.getDist());
        double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ZS.getDist());
        double[] power = sysSkill.getPower();
        double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
        int additional = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(effector.getLevel() * 3.0D * (1.0D - kx)));
        harmHp = SkillCompute.computeSkillDamage(proficient, power[0], target.getMaxLife(), currLife, kx, hkx, extra, weak, wx).intValue() + additional;
        harmMp = SkillCompute.computeSkillDamage(proficient, power[1], target.getMaxMana(), currMana, kx, hkx, extra, weak, wx).intValue();
      } 
      e.setType(100000);
      if (harmMp > 0)
        e.setList(Collections.singletonList(new FiedValue("mana", (byte)4, String.valueOf(harmMp)))); 
      e.setHurt(harmHp);
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(75.0D * (Math.pow(skill.getProficient().intValue(), 0.77D) * 5.0D / 100.0D + 1.0D) * 1.0D));
  }
}
