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

public class SKILL_83026 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    int count = 1;
    if (AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 31506))
      count++; 
    return count;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(83026));
    Effect effect = new Effect();
    if (skill != null) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83026);
      if (!target.isFrozen()) {
        int time = 2;
        double persent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 16.0D * (effector.getRelive() * 0.5D + 1.0D) * 
              PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
        int dist = PRO_DICT_TYPE.BURN_HURT.getDist();
        ImbueUtil.addOrUpdStatus(target.getImbueMap(), 2070, dist, time, persent);
        effect.setType(2070);
        effect.setTime(time);
      } else {
        effect.setType(2070);
        effect.setTime(0);
      } 
      skillRecord.setT(target.getId().intValue());
    } 
    return effect;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 4300;
  }
  
  public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
    effector.getSkillCoolMap().setv(String.valueOf(83026), Integer.valueOf(5));
    return true;
  }
}
