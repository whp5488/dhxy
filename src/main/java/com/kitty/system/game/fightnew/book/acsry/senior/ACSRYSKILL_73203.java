package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.role.domain.Role;

public class ACSRYSKILL_73203 extends AcsrySkillProcessor {
  public void passive(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown())
      return; 
    if (fightObject.isPlayer()) {
      double baseLifePCT = 0.1D;
      Role role = fightObject.getRole();
      Integer mj = role.getMj();
      baseLifePCT += (mj.intValue() / 200) * 0.01D;
      FightObject recordTarget = null;
      for (FightObject obj : fight.getMySide(fightObject.getId().intValue())) {
        if (obj.getStatusValue(1009) > 0) {
          int unitId = obj.getStatusSubMap().getInt("1009target");
          if (fightObject.getId().intValue() != unitId)
            continue; 
          if (recordTarget == null)
            recordTarget = obj; 
          int oldLife = obj.getCurrLife();
          int addLife = (int)(obj.getMaxLife() * baseLifePCT);
          if (addLife + oldLife > obj.getMaxLife())
            addLife = obj.getMaxLife() - obj.getCurrLife(); 
          if (addLife > 0)
            ActionParamUtils.changeLife(fight, fightObject, obj, 7, 300999, 2, addLife, false); 
        } 
      } 
      if (recordTarget != null) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 73203);
        skillRecord.setT(recordTarget.getId().intValue());
      } 
    } 
  }
  
  private void excute() {}
}
