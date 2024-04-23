package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.common.enter.FiedValue;
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
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class FU_322302 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public List<FiedValue> passiveFied(Fight fight, FightObject effector, FightObject target, Object... args) {
    DayInCeFight dayInCeFight;
    if (target.isPet()) {
      FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
      dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
    } else {
      dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
    } 
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322302);
    if (tianFuObject == null)
      return null; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 322302);
    double percenta = 1.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("hp_mp_addPercent", (byte)5, Double.valueOf(percenta)));
    skillRecord.setT(effector.getId().intValue());
    return list;
  }
}
