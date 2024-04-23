package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.role.domain.Role;

public class ACSRYSKILL_73213 extends AcsrySkillProcessor {
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
        if (obj.getStatusValue(1010) <= 0 || 
          obj.getMaxMana() <= obj.getCurrMana())
          continue; 
        int unitId = obj.getStatusSubMap().getInt("1010target");
        if (fightObject.getId().intValue() != unitId)
          continue; 
        if (recordTarget == null)
          recordTarget = obj; 
        int oldMana = obj.getCurrMana();
        int addMana = (int)(obj.getMaxMana() * baseLifePCT);
        if (addMana + oldMana > obj.getMaxMana())
          addMana = obj.getMaxMana() - obj.getCurrMana(); 
        ActionParamUtils.changeMana(fight, fightObject, obj, 7, 300999, 2, addMana);
      } 
      if (recordTarget != null) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 73213);
        skillRecord.setT(recordTarget.getId().intValue());
      } 
    } 
  }
}
