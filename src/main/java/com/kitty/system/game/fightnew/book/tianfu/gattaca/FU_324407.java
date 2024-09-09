package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.List;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324407 extends TianFuProcessor
{
    public FU_324407() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
        DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324407);
        if (tianFuObject == null) {
            return null;
        }
        boolean statusFlag = false;
        List<Integer> statusList = (List)args[0];
        if (statusList.size() > 0 && statusList.contains(Integer.valueOf(1008)) && (target.getStatusValue(1008) <= 0 || target.isFallDown())) {
            statusFlag = true;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324407);
        if (target.isPet() && statusFlag) {
            double percenta = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean triggerFlag = SkillAi.canAffectOnPro(percenta);
            if (triggerFlag) {
                List<FightObject> list = TianFuUtils.getFightObject(fight, target, Integer.valueOf(1008));
                if (list == null || list.size() <= 0) {
                    return null;
                }
                FightObject myTarget = (FightObject)list.get(0);
                int time = TianFuUtils.phyPowerUp(myTarget, Integer.valueOf(target.getStatusValue(1008)));
                myTarget.getStatusMap().setv(String.valueOf(1008), Integer.valueOf(time));
                ActionParamUtils.addStatus(fight, target, myTarget, 7, 300999, 2, 1008, time);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return null;
    }
}
