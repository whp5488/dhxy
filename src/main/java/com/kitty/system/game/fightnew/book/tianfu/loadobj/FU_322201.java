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
    public FU_322201() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (!effector.isPlayer()) {
            return null;
        }
        boolean flag = false;
        Integer theStatus = Integer.valueOf(-1);
        for (Integer status : StatusConst.getControlStatus()) {
            if (effector.getStatusValue((int)status) > 0) {
                flag = true;
                theStatus = status;
                break;
            }
            continue;
        }
        if (!flag) {
            return null;
        }
        Map<Integer, Integer> abnormalRoundMap = ((PlayerFightObject)effector).getAbnormalRoundMap();
        int round = fight.getRound();
        --round;
        Integer round1Status = (Integer)abnormalRoundMap.get(Integer.valueOf(round));
        --round;
        Integer round2Status = (Integer)abnormalRoundMap.get(Integer.valueOf(round));
        if (round1Status != null && StatusConst.getControlStatus().contains(round1Status) && round2Status != null && StatusConst.getControlStatus().contains(round2Status)) {
            DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
            TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322201);
            if (tianFuObject == null) {
                return null;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 322201);
            double percenta = 8.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
            if (flagTrigger) {
                effector.removeStatus((int)theStatus);
                List<Integer> clearList = new ArrayList<>();
                clearList.add(theStatus);
                ActionParamUtils.clearStatus(fight, effector, effector, 7, 300999, 2, clearList);
                skillRecord.setT((long)(int)effector.getId());
            }
        }
        return null;
    }
}
