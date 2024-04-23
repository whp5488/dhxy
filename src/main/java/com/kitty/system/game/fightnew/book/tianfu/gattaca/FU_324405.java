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
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class FU_324405 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
    DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324405);
    if (tianFuObject == null)
      return null; 
    boolean statusFlag = false;
    List<Integer> statusList = (List<Integer>)args[0];
    if (statusList.size() > 0 && statusList.contains(Integer.valueOf(1009)) && (
      target.getStatusValue(1009) <= 0 || target.isFallDown()))
      statusFlag = true; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324405);
    if (target.isPet() && statusFlag) {
      double percenta = 5.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      boolean triggerFlag = SkillAi.canAffectOnPro(percenta);
      if (triggerFlag) {
        List<FightObject> list = TianFuUtils.getFightObject(fight, target, Integer.valueOf(1009));
        if (list == null || list.size() <= 0)
          return null; 
        FightObject myTarget = list.get(0);
        int time = TianFuUtils.defUp(myTarget, Integer.valueOf(target.getStatusValue(1009)));
        myTarget.getStatusMap().setv(String.valueOf(1009), Integer.valueOf(time));
        ActionParamUtils.addStatus(fight, target, myTarget, 7, 300999, 2, 1009, time);
        skillRecord.setT(target.getId().intValue());
      } 
    } 
    return null;
  }
}
