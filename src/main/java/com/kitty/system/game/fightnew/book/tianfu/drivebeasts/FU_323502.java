package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323502 extends TianFuProcessor
{
    public FU_323502() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (effector.isPet() && effector.getComeRound() > 1) {
            FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
            if (host.isFallDown()) {
                return null;
            }
            DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
            TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323502);
            if (tianFuObject == null) {
                return null;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 323502);
            double percenta = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            double percentb = 2.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flag = SkillAi.canAffectOnPro(percenta);
            if (flag) {
                int time = TianFuUtils.defUp(effector, null);
                if (time <= 0) {
                    return null;
                }
                effector.getStatusMap().setv(String.valueOf(1009), Integer.valueOf(time));
                ActionParamUtils.addStatus(fight, effector, effector, 7, 300999, 2, 1009, time);
            }
            flag = SkillAi.canAffectOnPro(percenta);
            if (flag) {
                int time = TianFuUtils.speedUp(effector, null);
                if (time <= 0) {
                    return null;
                }
                effector.getStatusMap().setv(String.valueOf(1010), Integer.valueOf(time));
                ActionParamUtils.addStatus(fight, effector, effector, 7, 300999, 2, 1010, time);
            }
            flag = SkillAi.canAffectOnPro(percentb);
            if (flag) {
                int time = TianFuUtils.phyPowerUp(effector, null);
                if (time <= 0) {
                    return null;
                }
                effector.getStatusMap().setv(String.valueOf(1008), Integer.valueOf(time));
                ActionParamUtils.addStatus(fight, effector, effector, 7, 300999, 2, 1008, time);
                skillRecord.setT((long)(int)effector.getId());
            }
        }
        return null;
    }
}
