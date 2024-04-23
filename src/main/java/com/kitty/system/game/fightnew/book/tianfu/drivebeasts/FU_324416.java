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
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import org.nutz.lang.util.NutMap;

public class FU_324416 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (effector.isPet() && effector.getComeRound() > 1 && fight.getRound() == effector.getComeRound()) {
      FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
      DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
      TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324416);
      if (tianFuObject == null)
        return null; 
      double percent = 0.1D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D * fight.getRound();
      int phyPower = effector.getPhyPower();
      phyPower = (int)(phyPower + phyPower * percent);
      effector.setPhyPower(phyPower);
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324416);
      skillRecord.setT(effector.getId().intValue());
    } 
    return null;
  }
}
