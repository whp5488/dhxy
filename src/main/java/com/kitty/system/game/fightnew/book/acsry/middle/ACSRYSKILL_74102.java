
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74102 extends AcsrySkillProcessor
{
    @Override
    public NutMap countRecoverBloodEndSS(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(74103)) {
                return null;
            }
            if (playerFightObject.getAcsrySkillList().contains(74102)) {
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
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 74102);
                    skillRecord.setT(effecter.getId());
                    if (life > 0) {
                        final double percent = 1.0;
                        final int addLife = (int)(life * percent);
                        final NutMap map = NutMap.NEW();
                        map.setv("changeLife", (Object)addLife);
                        skillRecord.setT(effecter.getId());
                        return map;
                    }
                    return null;
                }
            }
        }
        return null;
    }
}
