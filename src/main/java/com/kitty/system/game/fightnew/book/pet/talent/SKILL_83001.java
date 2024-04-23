package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Arrays;
import org.nutz.lang.util.NutMap;

public class SKILL_83001 extends SkillProcessor {
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
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83001);
    int round = 2;
    if (args.length > 0 && args[0] instanceof Integer) {
      double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 4.0D * (effector.getRelive() * 0.4D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
      percent += AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31304, target);
      ImbueUtil.addRound(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.RISE_SS.getDist()), round, -percent);
      ImbueUtil.addRound(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), round, -percent);
      int hurt = ((Integer)args[0]).intValue();
      int addHurt = (int)(hurt * percent);
      Effect effect = new Effect(1012, round, hurt + addHurt, 0.0D, Arrays.asList(new com.kitty.system.common.enter.FiedValue[0]));
      effect.setTime(round);
      skillRecord.setT(target.getId().intValue());
      return effect;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
