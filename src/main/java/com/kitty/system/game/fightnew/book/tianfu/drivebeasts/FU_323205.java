
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.List;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323205 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (!target.isPet() || target.getHostId() == effector.getId() || fight.getRound() > 3 || fight.isPvp() != 1) {
            return null;
        }
        final FightObject host = fight.queryFightObject(target.getHostId());
        final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323205);
        if (tianFuObject == null) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 323205);
        final double percent = (5 + 4 * tianFuObject.getLevel()) * (1.0 + dayInCeFight.getLevel() * 0.002) / 100.0;
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            final FightObject unit = SkillAi.effectProBass(fight, target, 1, new ArrayList<String>());
            int addLife = 8480;
            final int oldLife = unit.getCurrLife();
            if (unit.getCurrLife() + addLife > unit.getMaxLife()) {
                addLife = unit.getMaxLife() - unit.getCurrLife();
            }
            boolean isRevive = false;
            if (oldLife <= 0) {
                isRevive = true;
            }
            ActionParamUtils.changeLife(fight, target, unit, 7, 300999, 2, addLife, isRevive);
            skillRecord.setT(unit.getId());
        }
        return null;
    }
}
