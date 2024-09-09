package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321403 extends TianFuProcessor
{
    public FU_321403() {
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
    public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (!target.isPlayer()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321403);
        if (tianFuObject == null) {
            return;
        }
        if (target.getStatusValue(1003) > 0) {
            return;
        }
        Integer hurt = (Integer)args[1];
        if (hurt == null || (int)hurt <= 0) {
            return;
        }
        if (target.getStatusValue(1008) <= 0) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 321403);
            if (target.getSkillCoolMap().getInt(String.valueOf(321403)) > 0) {
                return;
            }
            double percenta = 7.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
            if (!flagTrigger) {
                return;
            }
            int time = TianFuUtils.phyPowerUp(target, Integer.valueOf(3));
            if (time <= 0) {
                return;
            }
            target.getStatusMap().setv(String.valueOf(1008), Integer.valueOf(time));
            target.getSkillCoolMap().setv(String.valueOf(321403), Integer.valueOf(8));
            FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), false);
            if (targetParam != null) {
                targetParam.getStatusMap().put(Integer.valueOf(1008), Integer.valueOf(time));
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
}
