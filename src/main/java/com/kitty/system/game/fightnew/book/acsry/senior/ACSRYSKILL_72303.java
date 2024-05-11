
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_72303 extends AcsrySkillProcessor
{
    @Override
    public void passive(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isPlayer() && fight.getRound() == 1) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            final Role role = playerFightObject.getRole();
            final int base = 550;
            final Integer lx = role.getLx();
            final int baseParam = lx - base;
            if (baseParam <= 0) {
                return;
            }
            final int baseHurt = 40;
            int addHurt = baseParam * baseHurt;
            final int hurt = playerFightObject.getAcsryPropertyMap().getInt("baseHurt", 0);
            if (hurt > 0) {
                addHurt += hurt;
            }
            playerFightObject.getAcsryPropertyMap().setv("baseHurt", (Object)addHurt);
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 72303);
            skillRecord.setT(fightObject.getId());
        }
    }
}
