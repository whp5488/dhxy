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
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import org.nutz.lang.util.NutMap;

public class FU_323204 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (!target.isPet())
      return null; 
    FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
    if (host.isFallDown())
      return null; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323204);
    if (tianFuObject == null)
      return null; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 323204);
    if (target.isFallDown()) {
      double percenta = 1.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int addLife = (int)(host.getMaxLife() * percenta);
      if (host.getCurrLife() + addLife > host.getMaxLife())
        addLife = host.getMaxLife() - host.getCurrLife(); 
      ActionParamUtils.changeLife(fight, target, host, 7, 300999, 2, addLife, true);
      skillRecord.setT(host.getId().intValue());
    } 
    return null;
  }
}
