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
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;

public class FU_323205 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (!target.isPet() || target.getHostId() == effector.getId().intValue() || fight.getRound() > 3 || fight.isPvp() != 1)
      return null; 
    FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
    DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323205);
    if (tianFuObject == null)
      return null; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 323205);
    double percent = (5 + 4 * tianFuObject.getLevel()) * (1.0D + dayInCeFight.getLevel() * 0.002D) / 100.0D;
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      FightObject unit = SkillAi.effectProBass(fight, target, Integer.valueOf(1), new ArrayList());
      int addLife = 8480;
      int oldLife = unit.getCurrLife();
      if (unit.getCurrLife() + addLife > unit.getMaxLife())
        addLife = unit.getMaxLife() - unit.getCurrLife(); 
      boolean isRevive = false;
      if (oldLife <= 0)
        isRevive = true; 
      ActionParamUtils.changeLife(fight, target, unit, 7, 300999, 2, addLife, isRevive);
      skillRecord.setT(unit.getId().intValue());
    } 
    return null;
  }
}
