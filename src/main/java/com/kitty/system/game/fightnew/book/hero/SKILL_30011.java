package com.kitty.system.game.fightnew.book.hero;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_30011 extends SkillProcessor {
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
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30011));
    if (skill != null) {
      Effect e = new Effect();
      int harm = 0;
      if (!target.isFrozen()) {
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30011));
        int proficient = skill.getProficient().intValue();
        double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_L.getDist());
        double wsk = PassiveSkillUtils.getWsk(effector);
        kx -= wsk;
        double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
        double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_L.getDist());
        double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_L.getDist());
        double hurt = sysSkill.getPower()[0] * effector.getLevel() * (Math.pow(proficient, 0.4D) * 2.8853998118144273D / 100.0D + 1.0D);
        int chageBaseHurt = AcsrySkillUtil.getChageBaseHurt(effector);
        hurt += chageBaseHurt;
        double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
        harm = SkillCompute.computeSkillDamage(hurt, kx, hkx, extra, weak, wx).intValue();
      } 
      e.setType(100000);
      e.setHurt(harm);
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(25.0D * (Math.pow(skill.getProficient().intValue(), 0.77D) * 5.0D / 100.0D + 1.0D)));
  }
}
