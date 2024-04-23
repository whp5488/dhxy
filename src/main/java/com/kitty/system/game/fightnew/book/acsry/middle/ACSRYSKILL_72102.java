package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;

public class ACSRYSKILL_72102 extends AcsrySkillProcessor {
  public void passive(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
      if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(72103)))
        return; 
      if (fight.getRound() % 8 == 0) {
        double percent = 0.6D;
        playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Integer.valueOf(-1));
        playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Integer.valueOf(-1));
        playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Integer.valueOf(-1));
        playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Integer.valueOf(-1));
        playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Integer.valueOf(-1));
        playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_F.getDist() + "value", Double.valueOf(percent));
        playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_L.getDist() + "value", Double.valueOf(percent));
        playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_S.getDist() + "value", Double.valueOf(percent));
        playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_H.getDist() + "value", Double.valueOf(percent));
        playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_GH.getDist() + "value", Double.valueOf(percent));
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 72102);
        skillRecord.setT(fightObject.getId().intValue());
      } 
    } 
  }
}
