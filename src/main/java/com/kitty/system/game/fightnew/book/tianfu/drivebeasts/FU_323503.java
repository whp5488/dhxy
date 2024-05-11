
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323503 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (effector.isPet()) {
            final Double percent = this.getPercent(fight, effector);
            if (percent == null) {
                return null;
            }
            final int dist = PRO_DICT_TYPE.BURN_HURT.getDist();
            double value = ImbueUtil.getProValue(effector.getImbueMap(), dist);
            if (value < 0.3) {
                value += percent;
                if (value > 0.3) {
                    value = 0.3;
                }
                ImbueUtil.addOrUpdSkill(effector.getImbueMap(), dist, WarMaxRound.WAR_MAX_ROUND.getValue(), 323503, value);
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 323503);
            skillRecord.setT(effector.getId());
        }
        return null;
    }
    
    private Double getPercent(final Fight fight, final FightObject effector) {
        final FightObject host = fight.queryFightObject(effector.getHostId());
        final DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323503);
        if (tianFuObject == null) {
            return null;
        }
        final double percent = 0.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        return percent;
    }
}
