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
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class FU_322501 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (!target.isPlayer())
      return null; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322501);
    if (tianFuObject == null)
      return null; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 322501);
    if (getTriggerPercent(fight, target.getId())) {
      Integer hurt = (Integer)args[0];
      if (hurt == null || hurt.intValue() <= 0)
        return null; 
      double percenta = 4.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int alterHurt = (int)(hurt.intValue() * percenta);
      NutMap map = NutMap.NEW();
      map.setv("alterHurt", Integer.valueOf(-alterHurt));
      skillRecord.setT(target.getId().intValue());
      return map;
    } 
    return null;
  }
  
  public boolean getTriggerPercent(Fight fight, Integer targetId) {
    double percent = 0.2D;
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(targetId.intValue()).stream().filter(obj -> obj.isFallDown()).collect(Collectors.toList());
    if (mySide != null && mySide.size() > 0)
      percent += percent * mySide.size(); 
    return SkillAi.canAffectOnPro(percent);
  }
}
