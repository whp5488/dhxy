package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import org.nutz.lang.util.NutMap;

public class FU_321301 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill roleSkill = (RoleSkill)args[1];
    if (roleSkill == null || roleSkill.getSkillId() == null || roleSkill.getSkillId().intValue() < 1)
      return null; 
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(roleSkill.getSkillId());
    if (sysSkill == null)
      return null; 
    if (4 != sysSkill.getStep().intValue())
      return null; 
    if (!effector.isPlayer())
      return null; 
    int isPvp = fight.isPvp();
    if (0 == isPvp) {
      DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
      TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321301);
      if (tianFuObject == null)
        return null; 
      double percentb = 2.5D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int hurt = ((Integer)args[0]).intValue();
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 321301);
      skillRecord.setT(effector.getId().intValue());
      return TianFuUtils.getHurtReturnMap(hurt, percentb);
    } 
    if (1 == isPvp && fight.getRound() % 5 == 0) {
      DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
      TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321301);
      if (tianFuObject == null)
        return null; 
      double percenta = 5.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int hurt = ((Integer)args[0]).intValue();
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 321301);
      skillRecord.setT(effector.getId().intValue());
      return TianFuUtils.getHurtReturnMap(hurt, percenta);
    } 
    return null;
  }
}
