package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
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

public class FU_323104 extends TianFuProcessor
{
    public FU_323104() {
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
        FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
        DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323104);
        if (tianFuObject == null) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 323104);
        boolean flag = SkillAi.canAffectOnPro(0.1);
        if (flag) {
            Integer hurt = (Integer)args[0];
            if (hurt == null || (int)hurt <= 0) {
                return;
            }
            double percentb = 1.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int addLife = (int)((double)(int)hurt * percentb);
            if (addLife > 0) {
                if (addLife + effector.getCurrLife() > effector.getMaxLife()) {
                    addLife = effector.getMaxLife() - effector.getCurrLife();
                }
                FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)effector.getId(), true);
                PassiveSkillUtils.excuteChangeLifeAuto(fight, effector, actionDetailParam, targetParam, addLife);
                skillRecord.setT((long)(int)effector.getId());
            }
        }
        return;
    }
}
