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
import org.nutz.lang.util.NutMap;

public class SKILL_83003 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    int count = 1;
    if (AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 31314))
      count++; 
    return count;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83003);
    Effect effect = new Effect();
    if (!target.isFrozen()) {
      int time = 3;
      double percent = -BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 6.0D * (effector.getRelive() * 0.6D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
      double reduce = -BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 3.5D * (effector.getRelive() * 0.4D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1013, PRO_DICT_TYPE.K_FY.getDist(), time, reduce);
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1013, PRO_DICT_TYPE.K_HS.getDist(), time, reduce);
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1013, PRO_DICT_TYPE.K_HL.getDist(), time, reduce);
      String kfDist = String.valueOf(PRO_DICT_TYPE.K_F.getDist()) + 83003;
      String klDist = String.valueOf(PRO_DICT_TYPE.K_L.getDist()) + 83003;
      String ksDist = String.valueOf(PRO_DICT_TYPE.K_S.getDist()) + 83003;
      String khDist = String.valueOf(PRO_DICT_TYPE.K_H.getDist()) + 83003;
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1013, PRO_DICT_TYPE.K_F.getDist(), time, percent);
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1013, PRO_DICT_TYPE.K_L.getDist(), time, percent);
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1013, PRO_DICT_TYPE.K_S.getDist(), time, percent);
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1013, PRO_DICT_TYPE.K_H.getDist(), time, percent);
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1013, PRO_DICT_TYPE.WL_XS.getDist(), time, percent);
      effect.setTime(time);
      effect.setType(1013);
    } else {
      effect.setTime(0);
      effect.setType(1013);
    } 
    skillRecord.setT(target.getId().intValue());
    return effect;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    int level = PetSkillEffectorUtil.getExcuteLevel(args);
    return level * 32;
  }
}
