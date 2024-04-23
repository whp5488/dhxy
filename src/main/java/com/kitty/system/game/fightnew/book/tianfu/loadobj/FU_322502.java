package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
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
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Optional;
import org.nutz.lang.util.NutMap;

public class FU_322502 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (!target.isPlayer())
      return; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322502);
    if (tianFuObject == null)
      return; 
    RoleSkill roleSkill = (RoleSkill)args[0];
    if (roleSkill == null)
      return; 
    if ((target.isFallDown() || SKILLATTR.SKILLATTR_CONFUSE.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_ICE.getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_SLEEP
      .getValue() == roleSkill.getType().intValue() || SKILLATTR.SKILLATTR_YIWANG.getValue() == roleSkill.getType().intValue()) && 
      effector.getStatusValue(1016) <= 0) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 322502);
      Optional<FightObject> optional = fight.getMySide(effector.getId().intValue()).stream().filter(obj -> (obj.getSkillCoolMap().getInt(String.valueOf(322502)) > 0)).findFirst();
      if (optional.isPresent())
        return; 
      boolean flag = SkillAi.canAffectOnPro(0.2D);
      if (flag) {
        double percenta = 2.5D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
        int reduceSpeed = (int)(percenta * effector.getSpeed());
        String dist = String.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist());
        int time = 3;
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1016, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, -reduceSpeed);
        effector.getSkillCoolMap().setv(String.valueOf(322502), Integer.valueOf(6));
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId().intValue(), true);
        targetParam.getStatusMap().put(Integer.valueOf(1016), Integer.valueOf(time));
        skillRecord.setT(target.getId().intValue());
      } 
    } 
  }
}
