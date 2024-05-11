
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323204 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (!target.isPet()) {
            return null;
        }
        final FightObject host = fight.queryFightObject(target.getHostId());
        if (host.isFallDown()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323204);
        if (tianFuObject == null) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 323204);
        if (target.isFallDown()) {
            final double percenta = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int addLife = (int)(host.getMaxLife() * percenta);
            if (host.getCurrLife() + addLife > host.getMaxLife()) {
                addLife = host.getMaxLife() - host.getCurrLife();
            }
            ActionParamUtils.changeLife(fight, target, host, 7, 300999, 2, addLife, true);
            skillRecord.setT(host.getId());
        }
        return null;
    }
}
