
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75123 extends AcsrySkillProcessor
{
    @Override
    public NutMap beforePhysicalAttack(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        if (effecter.isPlayer()) {
            final PlayerFightObject playerTarget = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerTarget.getAcsrySkillList();
            if (acsrySkillList.contains(75123)) {
                final double percent = 0.4;
                final boolean flag = SkillAi.canAffectOnPro(percent);
                if (flag) {
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 75123);
                    skillRecord.setT(effecter.getId());
                    effecter.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist()), (Object)1.0);
                }
            }
        }
        return null;
    }
}
