package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

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

public class FU_324416 extends TianFuProcessor
{
    public FU_324416() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (effector.isPet() && effector.getComeRound() > 1 && fight.getRound() == effector.getComeRound()) {
            FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
            DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
            TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324416);
            if (tianFuObject == null) {
                return null;
            }
            double percent = 0.1 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0 * (double)fight.getRound();
            int phyPower = effector.getPhyPower();
            phyPower = (int)((double)phyPower + (double)phyPower * percent);
            effector.setPhyPower(phyPower);
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324416);
            skillRecord.setT((long)(int)effector.getId());
        }
        return null;
    }
}
