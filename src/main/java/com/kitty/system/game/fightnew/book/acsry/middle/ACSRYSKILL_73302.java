package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.AcsrySkillInsideUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;

public class ACSRYSKILL_73302 extends AcsrySkillProcessor {
  public void passive(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown())
      return; 
    if (fightObject.isPlayer()) {
      FightObject recordTarget = null;
      double percent = 0.1D;
      for (FightObject obj : fight.getMySide(fightObject.getId().intValue())) {
        if (obj.getStatusValue(1009) > 0) {
          int unitId = obj.getStatusSubMap().getInt("1009target");
          if (fightObject.getId().intValue() != unitId)
            continue; 
          if (SkillAi.canAffectOnPro(percent)) {
            if (obj.isFrozen()) {
              if (recordTarget == null)
                recordTarget = obj; 
              AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1003);
            } 
            if (obj.isForgotten()) {
              if (recordTarget == null)
                recordTarget = obj; 
              AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1006);
            } 
          } 
        } 
      } 
      if (recordTarget != null) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 73302);
        skillRecord.setT(recordTarget.getId().intValue());
      } 
    } 
  }
}
