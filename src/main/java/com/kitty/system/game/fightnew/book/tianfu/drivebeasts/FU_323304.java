
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323304 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (fight.getRound() > 5 || fight.isPvp() != 1 || !effector.isPet() || effector.getType() != 8) {
            return null;
        }
        final FightObject theTarget = fight.queryFightObject(effector.getTargetId());
        if (!theTarget.isPlayer() && !theTarget.isPartner()) {
            return null;
        }
        final FightObject host = fight.queryFightObject(effector.getHostId());
        final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323304);
        if (tianFuObject == null) {
            return null;
        }
        FightParamPool.addSkillRecord(fight, 7, effector.getId(), 323304);
        final double percenta = (45.0 + 2.5 * tianFuObject.getLevel()) * (1.0 + dayInCeFight.getLevel() * 0.002) / 100.0;
        final double percentb = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_PERCENT.getDist()), (Object)percenta);
        effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_CHANGE_PERCENT.getDist()), (Object)percentb);
        return null;
    }
}
