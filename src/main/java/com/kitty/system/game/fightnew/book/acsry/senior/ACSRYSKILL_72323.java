
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_72323 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            if (fight.getRound() % 2 == 1) {
                final int baseInt = 100;
                final double base = 0.03;
                double percent = 0.0;
                final double maxPercent = 0.8;
                final int round = 1;
                final Role role = playerFightObject.getRole();
                final Integer lx = role.getLx();
                final Integer gg = role.getGg();
                percent += lx / baseInt * base;
                percent += gg / baseInt * base;
                if (percent > maxPercent) {
                    percent = maxPercent;
                }
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), (Object)round);
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), (Object)round);
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), (Object)round);
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), (Object)round);
                playerFightObject.getAcsryPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), (Object)round);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_F.getDist() + "value", (Object)percent);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_L.getDist() + "value", (Object)percent);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_S.getDist() + "value", (Object)percent);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_H.getDist() + "value", (Object)percent);
                playerFightObject.getAcsryPropertyMap().setv(PRO_DICT_TYPE.OMIT_K_GH.getDist() + "value", (Object)percent);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, playerFightObject.getId(), 72323);
                skillRecord.setT(playerFightObject.getId());
            }
        }
    }
}
