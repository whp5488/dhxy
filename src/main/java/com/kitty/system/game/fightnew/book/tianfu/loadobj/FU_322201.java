package com.kitty.system.game.fightnew.book.tianfu.loadobj;

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
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nutz.lang.util.NutMap;

public class FU_322201 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (!effector.isPlayer())
      return null; 
    boolean flag = false;
    Integer theStatus = Integer.valueOf(-1);
    for (Integer status : StatusConst.getControlStatus()) {
      if (effector.getStatusValue(status.intValue()) > 0) {
        flag = true;
        theStatus = status;
        break;
      } 
    } 
    if (!flag)
      return null; 
    Map<Integer, Integer> abnormalRoundMap = ((PlayerFightObject)effector).getAbnormalRoundMap();
    int round = fight.getRound();
    round--;
    Integer round1Status = abnormalRoundMap.get(Integer.valueOf(round));
    round--;
    Integer round2Status = abnormalRoundMap.get(Integer.valueOf(round));
    if (round1Status != null && StatusConst.getControlStatus().contains(round1Status) && round2Status != null && 
      StatusConst.getControlStatus().contains(round2Status)) {
      DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
      TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322201);
      if (tianFuObject == null)
        return null; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 322201);
      double percenta = 8.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
      if (flagTrigger) {
        effector.removeStatus(theStatus.intValue());
        List<Integer> clearList = new ArrayList<>();
        clearList.add(theStatus);
        ActionParamUtils.clearStatus(fight, effector, effector, 7, 300999, 2, clearList);
        skillRecord.setT(effector.getId().intValue());
      } 
    } 
    return null;
  }
}
