
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321503 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (effector.isPlayer() && effector.getType() == 0) {
            final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321503);
            if (tianFuObject == null) {
                return null;
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321503);
            final double percenta = 2.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final boolean flagA = SkillAi.canAffectOnPro(percenta);
            if (flagA) {
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FURY.getDist()), (Object)3);
            }
            else {
                effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FURY.getDist()));
            }
            final double percentb = 3.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final boolean flagB = SkillAi.canAffectOnPro(percentb);
            if (flagB) {
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist()), (Object)1.2);
            }
            else {
                effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist()));
            }
            skillRecord.setT(target.getId());
        }
        return null;
    }
}
