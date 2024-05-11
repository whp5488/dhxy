
package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_322501 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (!target.isPlayer()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322501);
        if (tianFuObject == null) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 322501);
        if (!this.getTriggerPercent(fight, target.getId())) {
            return null;
        }
        final Integer hurt = (Integer)args[0];
        if (hurt == null || hurt <= 0) {
            return null;
        }
        final double percenta = 4.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        final int alterHurt = (int)(hurt * percenta);
        final NutMap map = NutMap.NEW();
        map.setv("alterHurt", (Object)(-alterHurt));
        skillRecord.setT(target.getId());
        return map;
    }
    
    public boolean getTriggerPercent(final Fight fight, final Integer targetId) {
        double percent = 0.2;
        final List<FightObject> mySide = fight.getMySide(targetId).stream().filter(obj -> obj.isFallDown()).collect(Collectors.toList());
        if (mySide != null && mySide.size() > 0) {
            percent += percent * mySide.size();
        }
        return SkillAi.canAffectOnPro(percent);
    }
}
