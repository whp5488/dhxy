package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
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
import org.nutz.lang.util.NutMap;

public class FU_321503 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (effector.isPlayer() && effector.getType() == 0) {
      DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
      TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321503);
      if (tianFuObject == null)
        return null; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 321503);
      double percenta = 2.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      boolean flagA = SkillAi.canAffectOnPro(percenta);
      if (flagA) {
        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FURY.getDist()), Integer.valueOf(3));
      } else {
        effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FURY.getDist()));
      } 
      double percentb = 3.5D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      boolean flagB = SkillAi.canAffectOnPro(percentb);
      if (flagB) {
        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist()), Double.valueOf(1.2D));
      } else {
        effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist()));
      } 
      skillRecord.setT(target.getId().intValue());
    } 
    return null;
  }
}
