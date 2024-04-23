package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;

public class ACSRYSKILL_75112 extends AcsrySkillProcessor {
  public void passive(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown())
      return; 
    int oldMana = fightObject.getCurrMana();
    int addMana = (int)(fightObject.getMaxMana() * 0.05D);
    if (oldMana + addMana > fightObject.getMaxMana())
      addMana = fightObject.getMaxMana() - fightObject.getCurrMana(); 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 75112);
    skillRecord.setT(fightObject.getId().intValue());
    ActionParamUtils.changeMana(fight, fightObject, fightObject, 7, 300999, 2, addMana);
  }
}
