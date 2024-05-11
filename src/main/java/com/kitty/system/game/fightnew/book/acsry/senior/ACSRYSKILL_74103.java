
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74103 extends AcsrySkillProcessor
{
    @Override
    public NutMap countRecoverBloodEndSS(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(74103)) {
                int life = 0;
                RoleSkill skill = null;
                try {
                    life = (int)args[0];
                    skill = (RoleSkill)args[1];
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                if (skill == null) {
                    return null;
                }
                if (SKILLATTR.SKILLATTR_XIXUE.getValue() == skill.getType()) {
                    if (life > 0) {
                        double percent = 1.0;
                        percent += playerFightObject.getRole().getGg() / 100 * 0.1;
                        final int addLife = (int)(life * percent);
                        final NutMap map = NutMap.NEW();
                        map.setv("changeLife", (Object)addLife);
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 74103);
                        skillRecord.setT(target.getId());
                        return map;
                    }
                    return null;
                }
            }
        }
        return null;
    }
}
