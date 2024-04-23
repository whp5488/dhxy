package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;

public class FU_321305 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    RoleSkill roleSkill = (RoleSkill)args[0];
    if (!effector.isPlayer())
      return; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321305);
    if (tianFuObject == null)
      return; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 321305);
    if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_YIWANG.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_SLEEP
      .getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_ICE.getValue() == roleSkill.getType().intValue()) {
      int hitCount = ((Integer)args[1]).intValue();
      if (hitCount <= 0)
        return; 
      double percenta = 0.2D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int addLife = (int)(effector.getMaxLife() * percenta * hitCount);
      TianFuUtils.whfmMethod(fight, effector, addLife, actionDetailParam);
      skillRecord.setT(effector.getId().intValue());
    } else {
      FightObject host;
      ArrayList<Integer> fallDownList = (ArrayList<Integer>)effector.getTempCacheValue("fallDownList");
      if (fallDownList == null || fallDownList.size() <= 0)
        return; 
      if (effector.isPet()) {
        host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
      } else {
        host = effector;
      } 
      double percentb = 1.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int addLife = (int)(host.getMaxLife() * percentb * fallDownList.size());
      TianFuUtils.whfmMethod(fight, host, addLife, actionDetailParam);
      skillRecord.setT(effector.getId().intValue());
    } 
  }
}
