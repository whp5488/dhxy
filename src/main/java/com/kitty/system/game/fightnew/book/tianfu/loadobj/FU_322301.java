
package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_322301 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public List<FiedValue> passiveFied(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        DayInCeFight dayInCeFight;
        if (target.isPet()) {
            final FightObject host = fight.queryFightObject(target.getHostId());
            dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        }
        else {
            dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        }
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322301);
        if (tianFuObject == null) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 322301);
        final double percenta = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        final double percentb = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("10022_addPercent", (byte)5, percenta));
        list.add(new FiedValue("10021_reducePercent", (byte)5, percentb));
        skillRecord.setT(target.getId());
        return list;
    }
}
