package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.WarMaxRound;
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
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class FU_324504 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (fight.isPvp() == 1) {
      RoleSkill roleSkill = (RoleSkill)args[1];
      if (roleSkill == null || roleSkill.getSkillId() == null || roleSkill.getSkillId().intValue() < 1)
        return null; 
      if (SKILLATTR.SKILLATTR_ZHEN.getValue() == roleSkill.getType().intValue()) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324504);
        Double percent = getPercent(effector);
        if (percent == null)
          return null; 
        ImbueParam imbueParam = ImbueUtil.gainProForSkill(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist()), 324504, true);
        double value = imbueParam.getValue() + percent.doubleValue();
        if (value > 0.3D)
          value = 0.3D; 
        imbueParam.setValue(value);
        imbueParam.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        skillRecord.setT(target.getId().intValue());
      } 
    } 
    return null;
  }
  
  private Double getPercent(FightObject effector) {
    if (!effector.isPlayer())
      return null; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324504);
    if (tianFuObject == null)
      return null; 
    double percent = 0.3D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
    return Double.valueOf(percent);
  }
}
