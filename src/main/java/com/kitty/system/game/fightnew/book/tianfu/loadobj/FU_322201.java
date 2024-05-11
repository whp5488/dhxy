
package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import java.util.Map;
import java.util.Iterator;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_322201 extends TianFuProcessor
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
        boolean flag = false;
        Integer theStatus = -1;
        for (final Integer status : StatusConst.getControlStatus()) {
            if (effector.getStatusValue(status) > 0) {
                flag = true;
                theStatus = status;
                break;
            }
        }
        if (!flag) {
            return null;
        }
        final Map<Integer, Integer> abnormalRoundMap = ((PlayerFightObject)effector).getAbnormalRoundMap();
        int round = fight.getRound();
        --round;
        final Integer round1Status = abnormalRoundMap.get(round);
        --round;
        final Integer round2Status = abnormalRoundMap.get(round);
        if (round1Status != null && StatusConst.getControlStatus().contains(round1Status) && round2Status != null && StatusConst.getControlStatus().contains(round2Status)) {
            final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322201);
            if (tianFuObject == null) {
                return null;
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 322201);
            final double percenta = 8.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
            if (flagTrigger) {
                effector.removeStatus(theStatus);
                final List<Integer> clearList = new ArrayList<Integer>();
                clearList.add(theStatus);
                ActionParamUtils.clearStatus(fight, effector, effector, 7, 300999, 2, clearList);
                skillRecord.setT(effector.getId());
            }
        }
        return null;
    }
}
