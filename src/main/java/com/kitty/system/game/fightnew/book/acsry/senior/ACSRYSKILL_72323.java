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
    public ACSRYSKILL_72323() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            if (fight.getRound() % 2 == 1) {
                int baseInt = 100;
                double base = 0.03;
                double percent = 0.0;
                double maxPercent = 0.8;
                int round = 1;
                Role role = playerFightObject.getRole();
                Integer lx = role.getLx();
                Integer gg = role.getGg();
                percent += (double)((int)lx / baseInt) * base;
                percent += (double)((int)gg / baseInt) * base;
                if (percent > maxPercent) {
                    percent = maxPercent;
                }
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
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)playerFightObject.getId(), 72323);
                skillRecord.setT((long)(int)playerFightObject.getId());
            }
        }
    }
}
