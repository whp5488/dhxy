package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.List;

public class ACSRYSKILL_71332 extends AcsrySkillProcessor {
  public void beforeExecuteMagic(Fight fight, FightObject effecter, FightObject target, Object... args) {
    if (effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
      if (acsrySkillList.contains(Integer.valueOf(71332))) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 71332);
        double percent = 0.5D;
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
          String dictStr = String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist());
          playerFightObject.getAcsryPropertyMap().setv(dictStr, Integer.valueOf(-1));
          double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_DU.getDist());
          playerFightObject.getAcsryPropertyMap().setv(dictStr + "value", Double.valueOf(kx));
          skillRecord.setT(target.getId().intValue());
        } 
      } 
    } 
  }
}
