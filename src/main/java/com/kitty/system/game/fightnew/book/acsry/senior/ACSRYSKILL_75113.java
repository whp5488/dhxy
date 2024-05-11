
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75113 extends AcsrySkillProcessor
{
    @Override
    public NutMap beforeReduceHurtMp(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        if (target.isPlayer()) {
            RoleSkill skill = null;
            int mp = 0;
            try {
                skill = (RoleSkill)args[0];
                mp = (int)args[1];
                if (skill == null || mp <= 0) {
                    return null;
                }
            }
            catch (final Exception e) {
                return null;
            }
            if (SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType()) {
                final PlayerFightObject playerTarget = (PlayerFightObject)target;
                final List<Integer> acsrySkillList = playerTarget.getAcsrySkillList();
                if (acsrySkillList.contains(75113)) {
                    final double percent = 0.3;
                    final int changeMp = (int)(mp * percent);
                    if (changeMp > 0) {
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 75113);
                        skillRecord.setT(target.getId());
                        return NutMap.NEW().setv("changeMana", (Object)(-changeMp));
                    }
                }
            }
        }
        return null;
    }
}
