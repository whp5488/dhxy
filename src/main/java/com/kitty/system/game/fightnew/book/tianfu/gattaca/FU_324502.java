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
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class FU_324502 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (fight.isPvp() != 1 || !effector.isPlayer())
      return; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324502);
    if (tianFuObject == null)
      return; 
    RoleSkill roleSkill = (RoleSkill)args[0];
    if (roleSkill == null)
      return; 
    if (target.isFrozen())
      return; 
    if (SKILLATTR.SKILLATTR_FIRE.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_WIND.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_THUNDER
      .getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_WATER.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_POISON
      .getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_AIHAO.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_POJIA
      .getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_HENGSAO.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_ZHENJI
      .getValue() == roleSkill.getType().intValue()) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324502);
      int currLife = target.getCurrLife();
      if (currLife <= 0)
        return; 
      double percenta = 1.5D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      int hurt = (int)(currLife * percenta);
      if (hurt > 0) {
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), true);
        PassiveSkillUtils.excuteChangeLifeAuto(fight, target, actionDetailParam, targetParam, -hurt);
        skillRecord.setT(target.getId().intValue());
      } 
    } 
  }
}
