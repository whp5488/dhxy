package com.kitty.system.game.fightnew.book.hero;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_30037 extends SkillProcessor {
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
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30037));
    if (skill != null) {
      Effect e = new Effect();
      SysSkill skill1 = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30037));
      if (!target.isFrozen()) {
        int maxLife = target.getMaxLife();
        double[] power = skill1.getPower();
        int baseLife = (int)power[0] + skill.getProficient().intValue() * 14 / 5;
        double addPct = power[1] + power[2] * skill.getProficient().intValue() / 25000.0D;
        double propertyAdd = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist()), 0.0D);
        if (propertyAdd != 0.0D)
          addPct += propertyAdd; 
        int perLife = (int)(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(addPct)) * maxLife);
        int time = time(effector, skill);
        target.getStatusSubMap().setv(String.valueOf(1022) + "value", Integer.valueOf(baseLife + perLife));
        e.setTime(time);
        e.setType(1022);
      } 
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(25.0D * (Math.pow(skill.getProficient().intValue(), 0.77D) * 5.0D / 100.0D + 1.0D) * 1.0D));
  }
  
  public int time(FightObject effector, RoleSkill skill) {
    int time = 2;
    if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && 
      skill != null)
      time += BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(Math.pow(skill.getProficient().intValue(), 0.35D) * 5.0D / 100.0D)); 
    return time;
  }
}
