package com.kitty.system.game.fightnew.book.tianfu.gattaca;

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
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import org.nutz.lang.util.NutMap;

public class FU_321501 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill roleSkill = (RoleSkill)args[1];
    if (roleSkill == null || roleSkill.getSkillId() == null || roleSkill.getSkillId().intValue() < 1)
      return null; 
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(roleSkill.getSkillId());
    if (sysSkill == null)
      return null; 
    double lifePercent = (effector.getCurrLife() / effector.getMaxLife());
    if (lifePercent >= 1.0D)
      return null; 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 321501);
    double percent = 1.0D - lifePercent;
    boolean flag = SkillAi.canAffectOnPro(percent);
    Double percenta = getPercent(effector);
    if (percenta == null)
      return null; 
    if (flag && (30001 == sysSkill.getSerial().intValue() || 30002 == sysSkill.getSerial().intValue())) {
      installProperty(effector, PRO_DICT_TYPE.K_HL.getDist(), percenta);
      skillRecord.setT(effector.getId().intValue());
    } else if (flag && (30003 == sysSkill.getSerial().intValue() || 30004 == sysSkill.getSerial().intValue())) {
      installProperty(effector, PRO_DICT_TYPE.K_HS.getDist(), percenta);
      skillRecord.setT(effector.getId().intValue());
    } else if (flag && (30005 == sysSkill.getSerial().intValue() || 30006 == sysSkill.getSerial().intValue())) {
      installProperty(effector, PRO_DICT_TYPE.K_FY.getDist(), percenta);
      skillRecord.setT(effector.getId().intValue());
    } else if (flag && (30029 == sysSkill.getSerial().intValue() || 30030 == sysSkill.getSerial().intValue())) {
      installProperty(effector, PRO_DICT_TYPE.K_YW.getDist(), percenta);
      skillRecord.setT(effector.getId().intValue());
    } 
    return null;
  }
  
  private Double getPercent(FightObject effector) {
    if (!effector.isPlayer())
      return null; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321501);
    if (tianFuObject == null)
      return null; 
    double percent = 0.3D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
    return Double.valueOf(percent);
  }
  
  private void installProperty(FightObject effector, int dist, Double percenta) {
    String distStr = String.valueOf(dist);
    double property = effector.getPropertyMap().getDouble(distStr);
    if (property > 0.0D) {
      property += percenta.doubleValue();
    } else {
      property = percenta.doubleValue();
    } 
    effector.getPropertyMap().setv(distStr, Double.valueOf(property));
  }
}
