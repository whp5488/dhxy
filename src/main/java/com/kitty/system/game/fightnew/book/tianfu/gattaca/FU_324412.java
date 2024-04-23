package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.wardef.RACE;
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
import org.nutz.lang.util.NutMap;

public class FU_324412 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (fight.isPvp() != 1 || effector.getRace() != RACE.RACE_REN.getValue())
      return; 
    RoleSkill roleSkill = (RoleSkill)args[0];
    Integer count = (Integer)args[1];
    if (roleSkill == null || SKILLATTR.SKILLATTR_CONFUSE.getValue() != roleSkill.getType().intValue() || count == null || count.intValue() <= 0)
      return; 
    if (!effector.isPlayer())
      return; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324412);
    if (tianFuObject == null)
      return; 
    double percenta = 0.8D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324412);
    int addLife = (int)(count.intValue() * percenta * effector.getMaxLife());
    TianFuUtils.whfmMethod(fight, effector, addLife, actionDetailParam);
    skillRecord.setT(effector.getId().intValue());
  }
}
