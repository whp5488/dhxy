
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_72102 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            if (playerFightObject.getAcsrySkillList().contains(72103)) {
                return;
            }
            if (fight.getRound() % 8 == 0) {
                final double percent = 0.6;
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), (Object)(-1));
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), (Object)(-1));
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), (Object)(-1));
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), (Object)(-1));
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), (Object)(-1));
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_F.getDist() + "value", (Object)percent);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_L.getDist() + "value", (Object)percent);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_S.getDist() + "value", (Object)percent);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_H.getDist() + "value", (Object)percent);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_GH.getDist() + "value", (Object)percent);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 72102);
                skillRecord.setT(fightObject.getId());
            }
        }
    }
}
