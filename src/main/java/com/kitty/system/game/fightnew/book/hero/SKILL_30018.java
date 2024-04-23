package com.kitty.system.game.fightnew.book.hero;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_30018 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30018));
    if (skill != null && 
      skill != null)
      return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(skill.getProficient().intValue(), 0.3D) * 8.0D / 100.0D)))); 
    return 0;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30018));
    int speed = target.getSpeed();
    if (skill != null) {
      Effect e = new Effect();
      if (!target.isFrozen()) {
        int time = time(effector, skill);
        if (target.getStatusMap().getInt(String.valueOf(1011)) > 0) {
          time = 0;
        } else {
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, (speed < 0) ? 0.0D : 
              (int)(imbue(skill.getProficient().intValue()) * target.getSpeed()));
        } 
        e.setTime(time);
        e.setType(1010);
      } 
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(75.0D * (Math.pow(skill.getProficient().intValue(), 0.77D) * 5.0D / 100.0D + 1.0D) * 1.0D));
  }
  
  public int time(FightObject effector, RoleSkill skill) {
    int time = 3;
    if (skill != null)
      time = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(skill.getProficient().intValue(), 0.35D) * 5.0D / 100.0D))); 
    return time;
  }
  
  public double imbue(int proficient) {
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30018));
    return BigDecimalUtil.decimalScaleDouble(Integer.valueOf(2), Double.valueOf((sysSkill.getPower()[0] + (8 * proficient) / 25000.0D) / 100.0D));
  }
}
