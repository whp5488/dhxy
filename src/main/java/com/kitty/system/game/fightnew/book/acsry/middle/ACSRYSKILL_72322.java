package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_72322 extends AcsrySkillProcessor
{
    public ACSRYSKILL_72322() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isPlayer()) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 72322);
            PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            double percent = 0.25;
            if (fight.getRound() % 2 == 0) {
                percent = -0.15;
            }
            int round = 1;
            playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Integer.valueOf(round));
            playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Integer.valueOf(round));
            playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Integer.valueOf(round));
            playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Integer.valueOf(round));
            playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Integer.valueOf(round));
            playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_F.getDist() + "value", Double.valueOf(percent));
            playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_L.getDist() + "value", Double.valueOf(percent));
            playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_S.getDist() + "value", Double.valueOf(percent));
            playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_H.getDist() + "value", Double.valueOf(percent));
            playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_GH.getDist() + "value", Double.valueOf(percent));
            skillRecord.setT((long)(int)fightObject.getId());
        }
    }
}
