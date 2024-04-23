package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

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
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class FU_323104 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
    DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323104);
    if (tianFuObject == null)
      return; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 323104);
    boolean flag = SkillAi.canAffectOnPro(0.1D);
    if (flag) {
      Integer hurt = (Integer)args[0];
      if (hurt == null || hurt.intValue() <= 0)
        return; 
      double percentb = 1.5D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int addLife = (int)(hurt.intValue() * percentb);
      if (addLife > 0) {
        if (addLife + effector.getCurrLife() > effector.getMaxLife())
          addLife = effector.getMaxLife() - effector.getCurrLife(); 
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId().intValue(), true);
        PassiveSkillUtils.excuteChangeLifeAuto(fight, effector, actionDetailParam, targetParam, addLife);
        skillRecord.setT(effector.getId().intValue());
      } 
    } 
  }
}
