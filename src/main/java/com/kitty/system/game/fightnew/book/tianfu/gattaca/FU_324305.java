package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.wardef.RACE;
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
import org.nutz.lang.util.NutMap;

public class FU_324305 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (RACE.RACE_MO.getValue() == effector.getRace() || RACE.RACE_LONG.getValue() == effector.getRace()) {
      if (!effector.isPlayer())
        return null; 
      DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
      TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324305);
      if (tianFuObject == null)
        return null; 
      Integer mp = (Integer)args[0];
      if (mp == null || mp.intValue() <= 0)
        return null; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324305);
      double percenta = 3.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int alterMp = (int)(mp.intValue() * percenta);
      NutMap map = NutMap.NEW();
      map.setv("alterMp", Integer.valueOf(-alterMp));
      skillRecord.setT(effector.getId().intValue());
      return map;
    } 
    return null;
  }
}
