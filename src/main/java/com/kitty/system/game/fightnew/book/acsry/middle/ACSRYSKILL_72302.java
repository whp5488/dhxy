package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_72302 extends AcsrySkillProcessor
{
    public ACSRYSKILL_72302() {
    }
    
    @Override
    public void passive(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isPlayer() && fight.getRound() == 1) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 72302);
            PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            Role role = playerFightObject.getRole();
            int base = 550;
            Integer lx = role.getLx();
            int baseParam = (int)lx - base;
            if (baseParam <= 0) {
                return;
            }
            int baseHurt = 20;
            int addHurt = baseParam * baseHurt;
            int hurt = playerFightObject.getAcsryPropertyMap().getInt("baseHurt", 0);
            if (hurt > 0) {
                addHurt += hurt;
            }
            playerFightObject.getAcsryPropertyMap().setv("baseHurt", Integer.valueOf(addHurt));
            skillRecord.setT((long)(int)fightObject.getId());
        }
    }
}
