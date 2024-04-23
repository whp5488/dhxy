package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
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

public class FU_322204 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (!effector.isPlayer())
      return null; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322204);
    if (tianFuObject == null)
      return null; 
    Integer status = (Integer)args[0];
    if (status != null && (1002 == status.intValue() || 1003 == status.intValue() || 1004 == status
      .intValue() || 1006 == status.intValue())) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 322204);
      double percenta = 1.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int dist = PRO_DICT_TYPE.SH_DK.getDist();
      int time = ImbueUtil.getProRound(effector.getImbueMap(), Integer.valueOf(dist));
      if (time <= 1) {
        ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(dist), 1, percenta);
        skillRecord.setT(effector.getId().intValue());
      } 
    } 
    return null;
  }
}
