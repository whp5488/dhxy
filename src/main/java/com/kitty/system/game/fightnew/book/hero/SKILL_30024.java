package com.kitty.system.game.fightnew.book.hero;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
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

public class SKILL_30024 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30024));
    if (skill != null && 
      skill != null)
      return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(skill.getProficient().intValue(), 0.3D) * 8.0D / 100.0D)))); 
    return 3;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30024));
    if (skill != null) {
      Effect e = new Effect();
      if (!target.isFrozen()) {
        int time = time(effector, skill);
        if (target.getStatusMap().getInt(String.valueOf(1011)) > 0) {
          time = 0;
        } else {
          SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30024));
          int proficient = skill.getProficient().intValue();
          double[] power = sysSkill.getPower();
          double up = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(power[0] * (Math.pow(proficient, 0.35D) * 20.0D / 100.0D + 1.0D)));
          double buff = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[1] * (Math.pow(proficient, 0.35D) * 2.0D / 100.0D + 1.0D)));
          double add = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[2] * (Math.pow(proficient, 0.35D) * 2.0D / 100.0D + 1.0D)));
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_FY.getDist(), time, up);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_HS.getDist(), time, up);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_HL.getDist(), time, up);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_YW.getDist(), time, up);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_F.getDist(), time, buff);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_L.getDist(), time, buff);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_S.getDist(), time, buff);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_H.getDist(), time, buff);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_SS.getDist(), time, buff);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_GH.getDist(), time, buff);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_DU.getDist(), time, buff);
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1009, PRO_DICT_TYPE.WL_XS.getDist(), time, add);
        } 
        e.setTime(time);
        e.setType(1009);
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
    if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && 
      skill != null)
      time = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(skill.getProficient().intValue(), 0.35D) * 5.0D / 100.0D))); 
    return time;
  }
}
