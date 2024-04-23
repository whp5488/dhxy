package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuSkillUtils;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class FU_321302 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill roleSkill = (RoleSkill)args[1];
    Effect effect = (Effect)args[2];
    if (!effector.isPlayer() || effect == null || roleSkill == null)
      return null; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321302);
    if (tianFuObject == null)
      return null; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 321302);
    if (SKILLATTR.SKILLATTR_POISON.getValue() == roleSkill.getType().intValue() && target.getStatusValue(1005) > 0) {
      int hurt = ((Integer)args[0]).intValue();
      double percentb = 0.6D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      double addPercent = addPercent(target, percentb, String.valueOf(OTHER_SKILL_DICT_TYPE.D_SUPERPOSITION_COUNT.getDist()));
      percentb += addPercent;
      skillRecord.setT(target.getId().intValue());
      return TianFuUtils.getHurtReturnMap(hurt, percentb);
    } 
    if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == roleSkill.getType().intValue() && target.getStatusValue(1007) > 0) {
      double percenta = 0.5D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      double addPercent = addPercent(target, percenta, String.valueOf(OTHER_SKILL_DICT_TYPE.MH_SUPERPOSITION_COUNT.getDist()));
      percenta += addPercent;
      TianFuSkillUtils.mhChangePro(target, 1007, effect.getTime(), percenta);
      skillRecord.setT(target.getId().intValue());
    } 
    return null;
  }
  
  private double addPercent(FightObject target, double percent, String superpositionStr) {
    int maxSuerpositionCount = 4;
    int dSuerposition = target.getBeAttackedMap().getInt(superpositionStr, 0);
    if (dSuerposition > 0) {
      if (dSuerposition > maxSuerpositionCount)
        dSuerposition = maxSuerpositionCount; 
      target.getBeAttackedMap().setv(superpositionStr, Integer.valueOf(++dSuerposition));
      return dSuerposition * percent;
    } 
    target.getBeAttackedMap().setv(superpositionStr, Integer.valueOf(++dSuerposition));
    return percent;
  }
}
