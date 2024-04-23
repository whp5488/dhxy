package com.kitty.system.game.fightnew.book.hero;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import java.util.Collections;
import org.nutz.lang.util.NutMap;

public class SKILL_30036 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30036));
    if (skill != null)
      return (int)(1.2D * (1.0D + Math.pow(skill.getProficient().intValue(), 0.35D) * 5.0D / 100.0D)); 
    return 0;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(30036));
    if (skill != null) {
      Effect e = new Effect();
      if (!target.isFrozen()) {
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30036));
        int atk = effector.getPhyPower() + (int)(effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * effector.getPhyPower());
        double conquer = effector.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_RATIO.getDist());
        double conquer_extent = effector.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_EXTENT.getDist());
        double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
        double[] power = sysSkill.getPower();
        atk += (int)(power[0] * effector.getLevel());
        double mark = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(1), Double.valueOf(power[1] + power[2] * (1.0D + Math.pow(skill.getProficient().intValue(), 0.35D) * 2.0D / 100.0D))) / 100.0D));
        ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), 1, mark);
        conquer += mark;
        conquer_extent += mark;
        if (weak < 0.0D)
          weak = 0.0D; 
        boolean flag = SkillAi.canAffectOnPro(conquer);
        Integer hurt = SkillCompute.computeDragonSkillDamage(fight, effector, target, atk, conquer_extent, flag, effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_QHS.getDist()), weak);
        e.setList(Collections.singletonList(new FiedValue("penetrate", (byte)3, Boolean.valueOf(flag))));
        e.setHurt(hurt.intValue());
        e.setType(1024);
      } 
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(75.0D * (Math.pow(skill.getProficient().intValue(), 0.77D) * 5.0D / 100.0D + 1.0D) * 1.0D));
  }
}
