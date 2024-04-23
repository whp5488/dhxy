package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class ACSRYSKILL_75103 extends AcsrySkillProcessor {
  public NutMap afterPrintCommand(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown() && fightObject.getDieRound() <= fight.getRound() - 1) {
      double percent = 0.1D;
      boolean flag = SkillAi.canAffectOnPro(percent);
      if (flag) {
        double addHpPercent = 0.3D;
        int addHp = (int)(fightObject.getMaxLife() * addHpPercent);
        ActionParamUtils.changeLife(fight, fightObject, fightObject, 7, 300999, 2, addHp, true);
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 75103);
        skillRecord.setT(fightObject.getId().intValue());
      } 
    } 
    return null;
  }
}
