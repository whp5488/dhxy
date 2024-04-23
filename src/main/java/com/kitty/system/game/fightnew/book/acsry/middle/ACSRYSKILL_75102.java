package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;

public class ACSRYSKILL_75102 extends AcsrySkillProcessor {
  public void passive(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown())
      return; 
    int oldLife = fightObject.getCurrLife();
    int addLife = (int)(fightObject.getMaxLife() * 0.05D);
    if (oldLife + addLife > fightObject.getMaxLife())
      addLife = fightObject.getMaxLife() - fightObject.getCurrLife(); 
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 75102);
    skillRecord.setT(fightObject.getId().intValue());
    ActionParamUtils.changeLife(fight, fightObject, fightObject, 7, 300999, 2, addLife, false);
  }
}
