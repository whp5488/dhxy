package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class FU_321402 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (!target.isPlayer())
      return; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321402);
    if (tianFuObject == null)
      return; 
    if (target.getStatusValue(1003) > 0)
      return; 
    Integer hurt = (Integer)args[1];
    if (hurt == null || hurt.intValue() <= 0)
      return; 
    if (target.getStatusValue(1010) <= 0) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 321402);
      if (target.getSkillCoolMap().getInt(String.valueOf(321402)) > 0)
        return; 
      double percenta = 7.5D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
      if (!flagTrigger)
        return; 
      int time = TianFuUtils.speedUp(target, Integer.valueOf(3));
      if (time <= 0)
        return; 
      target.getStatusMap().setv(String.valueOf(1010), Integer.valueOf(time));
      target.getSkillCoolMap().setv(String.valueOf(321402), Integer.valueOf(8));
      FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), false);
      if (targetParam != null) {
        targetParam.getStatusMap().put(Integer.valueOf(1010), Integer.valueOf(time));
        skillRecord.setT(target.getId().intValue());
      } 
    } 
  }
}
