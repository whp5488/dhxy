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
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class FU_324507 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (effector.getRace() == RACE.RACE_LONG.getValue()) {
      DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
      TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324507);
      if (tianFuObject == null)
        return null; 
      double percenta = 5.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      boolean flag = SkillAi.canAffectOnPro(percenta);
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324507);
      if (flag) {
        int time = effector.getSkillCoolMap().getInt(String.valueOf(30041));
        if (time > 0) {
          effector.getSkillCoolMap().setv(String.valueOf(30041), Integer.valueOf(time - 1));
          skillRecord.setT(effector.getId().intValue());
        } 
      } 
    } 
    return null;
  }
}
