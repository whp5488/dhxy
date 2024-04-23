package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import org.nutz.lang.util.NutMap;

public class FU_323503 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (effector.isPet()) {
      Double percent = getPercent(fight, effector);
      if (percent == null)
        return null; 
      int dist = PRO_DICT_TYPE.BURN_HURT.getDist();
      double value = ImbueUtil.getProValue(effector.getImbueMap(), Integer.valueOf(dist));
      if (value < 0.3D) {
        value += percent.doubleValue();
        if (value > 0.3D)
          value = 0.3D; 
        ImbueUtil.addOrUpdSkill(effector.getImbueMap(), Integer.valueOf(dist), WarMaxRound.WAR_MAX_ROUND.getValue(), 323503, value);
      } 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 323503);
      skillRecord.setT(effector.getId().intValue());
    } 
    return null;
  }
  
  private Double getPercent(Fight fight, FightObject effector) {
    FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
    DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323503);
    if (tianFuObject == null)
      return null; 
    double percent = 0.5D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
    return Double.valueOf(percent);
  }
}
