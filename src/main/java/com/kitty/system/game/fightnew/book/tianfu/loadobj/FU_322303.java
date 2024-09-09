package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
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

public class FU_322303 extends TianFuProcessor
{
    public FU_322303() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public List<FiedValue> passiveFied(Fight fight, FightObject effector, FightObject target, Object... args) {
        DayInCeFight dayInCeFight;
        if (target.isPet()) {
            FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
            dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        }
        else {
            dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        }
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322303);
        if (tianFuObject == null) {
            return null;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 322303);
        double percenta = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        boolean flag = SkillAi.canAffectOnPro(percenta);
        List<FiedValue> list = new ArrayList<>();
        list.add(new FiedValue("addTarget", (byte)6, Boolean.valueOf(flag)));
        skillRecord.setT((long)(int)effector.getId());
        return list;
    }
}
