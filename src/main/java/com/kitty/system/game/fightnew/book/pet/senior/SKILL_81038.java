package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.common.enter.FiedValue;
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
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_81038 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    List<FightObject> targetSide = fight.getTargetSide(effector.getId().intValue());
    return targetSide.size();
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81038);
    int time = 3;
    double increment = 50.0D;
    Effect effect = new Effect();
    if (!target.isFrozen()) {
      List<Integer> clearList = PetSkillEffectorUtil.clearWuXin(target);
      if (clearList != null)
        effect.getOrCreateList().add(new FiedValue("targetClearStatus", (byte)7, clearList)); 
      int add = (int)AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31411, target);
      if (add > 0)
        increment += add; 
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 2062, PRO_DICT_TYPE.WX_J.getDist(), time, increment);
      effect.setType(2062);
      effect.setTime(time);
      skillRecord.setT(target.getId().intValue());
    } 
    return effect;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    int level = PetSkillEffectorUtil.getExcuteLevel(args);
    return level * 21;
  }
  
  public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
    int skillCoolTime = 5;
    effector.getSkillCoolMap().setv(String.valueOf(81038), Integer.valueOf(skillCoolTime));
    return true;
  }
}
