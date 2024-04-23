package com.kitty.system.game.fightnew.book.hero;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_30008 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30008));
    if (skill != null && 
      skill != null)
      return Math.min(7, BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(skill.getProficient().intValue(), 0.35D) * 5.0D / 100.0D)))); 
    return 0;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30008));
    if (skill != null) {
      Effect e = new Effect();
      if (!target.isFrozen()) {
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30008));
        double power = sysSkill.getPower()[0];
        int proficient = skill.getProficient().intValue();
        int maxLife = target.getMaxLife();
        double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
        double kdx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_DU.getDist());
        double wsk = PassiveSkillUtils.getWsk(effector);
        kdx -= wsk;
        double dqd = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_D.getDist());
        double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_D.getDist());
        double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
        double hurt = SkillCompute.computeSkillDamagePoison(power, kdx, dqd, Integer.valueOf(proficient), Integer.valueOf(1), Integer.valueOf(effector.getLevel()), Integer.valueOf(maxLife), extra, weak, wx);
        int time = time(effector, skill, kdx, dqd);
        target.getStatusSubMap().setv("1005_power", Double.valueOf(power));
        target.getStatusSubMap().setv("1005_kdx", Double.valueOf(kdx));
        target.getStatusSubMap().setv("1005_weak", Double.valueOf(weak));
        target.getStatusSubMap().setv("1005_dqd", Double.valueOf(dqd));
        target.getStatusSubMap().setv("1005_proficient", Integer.valueOf(proficient));
        target.getStatusSubMap().setv("1005_round", Integer.valueOf(1));
        target.getStatusSubMap().setv("1005_level", Integer.valueOf(effector.getLevel()));
        target.getStatusSubMap().setv("1005_maxLife", Integer.valueOf(maxLife));
        target.getStatusSubMap().setv("1005_extra", Double.valueOf(extra));
        target.getStatusSubMap().setv("1005_wx", Double.valueOf(wx));
        e.setHurt(Math.max(1, (int)hurt));
        e.setTime(time);
        e.setType(1005);
      } 
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(75.0D * (Math.pow(skill.getProficient().intValue(), 0.77D) * 5.0D / 100.0D + 1.0D)));
  }
  
  public int time(FightObject effector, RoleSkill skill, double kdx, double dqd) {
    int time = 2;
    if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue() && 
      skill != null)
      time = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(2.0D * (1.0D + Math.pow(skill.getProficient().intValue(), 0.34D) * 4.0D / 100.0D))); 
    return Math.min(time, BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf(time * (1.0D - kdx / 2.0D + dqd))));
  }
}
