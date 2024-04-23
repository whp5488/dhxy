package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_830341 extends SkillProcessor {
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
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(830341));
    if (skill != null) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 830341);
      int time = 3;
      Effect e = new Effect();
      if (!target.isFrozen()) {
        List<Integer> clearList = PetSkillEffectorUtil.clearWuXin(target);
        if (clearList != null)
          e.getOrCreateList().add(new FiedValue("targetClearStatus", (byte)7, clearList)); 
        double persent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf(50.0D + 4.6D * (effector.getRelive() * 0.5D + 1.0D) * 
              PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
        ImbueUtil.addOrUpdStatus(target.getImbueMap(), 2077, PRO_DICT_TYPE.WX_J.getDist(), time, persent);
        e.setType(2077);
        e.setTime(time);
      } 
      skillRecord.setT(target.getId().intValue());
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    int level = PetSkillEffectorUtil.getExcuteLevel(args);
    return level * 21;
  }
  
  public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
    int skillCoolTime = 5;
    effector.getSkillCoolMap().setv(String.valueOf(83034), Integer.valueOf(skillCoolTime));
    return true;
  }
}
