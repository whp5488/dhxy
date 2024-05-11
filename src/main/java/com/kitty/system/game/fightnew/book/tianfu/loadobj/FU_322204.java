
package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_322204 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (!effector.isPlayer()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322204);
        if (tianFuObject == null) {
            return null;
        }
        final Integer status = (Integer)args[0];
        if (status != null && (1002 == status || 1003 == status || 1004 == status || 1006 == status)) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 322204);
            final double percenta = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final int dist = PRO_DICT_TYPE.SH_DK.getDist();
            final int time = ImbueUtil.getProRound(effector.getImbueMap(), dist);
            if (time <= 1) {
                ImbueUtil.addRound(effector.getImbueMap(), dist, 1, percenta);
                skillRecord.setT(effector.getId());
            }
        }
        return null;
    }
}
