package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.AcsrySkillInsideUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.role.domain.Role;

public class ACSRYSKILL_73303 extends AcsrySkillProcessor {
  public void passive(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown())
      return; 
    if (fightObject.isPlayer()) {
      double percent = 0.1D;
      Role role = fightObject.getRole();
      percent += (role.getMj().intValue() / 200) * 0.01D;
      FightObject recordTarget = null;
      for (FightObject obj : fight.getMySide(fightObject.getId().intValue())) {
        if (obj.getStatusValue(1009) > 0) {
          int unitId = obj.getStatusSubMap().getInt("1009target");
          if (fightObject.getId().intValue() != unitId)
            continue; 
          if (SkillAi.canAffectOnPro(percent)) {
            if (obj.isFrozen()) {
              AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1003);
              if (recordTarget == null)
                recordTarget = obj; 
            } 
            if (obj.isForgotten()) {
              AcsrySkillInsideUtil.excuteClearStatus(fight, fightObject, obj, 1006);
              if (recordTarget == null)
                recordTarget = obj; 
            } 
          } 
        } 
      } 
      if (recordTarget != null) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 73303);
        skillRecord.setT(recordTarget.getId().intValue());
      } 
    } 
  }
}
