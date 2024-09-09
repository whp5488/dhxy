package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324418 extends TianFuProcessor
{
    public FU_324418() {
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
            TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324418);
            if (tianFuObject == null) {
                return null;
            }
            double percent = 0.1 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0 * (double)fight.getRound();
            int fl = effector.getMaxMana();
            if (fl > 0) {
                fl = (int)((double)fl + (double)fl * percent);
                effector.setMaxMana(fl);
                effector.setCurrMana(fl);
                ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 300999, (int)effector.getId());
                FightObjectParam targetParam = new FightObjectParam();
                targetParam.setCurrentMana(effector.getCurrMana());
                targetParam.setMaxMana(effector.getMaxMana());
                targetParam.setMissed(2);
                targetParam.setObjId((int)effector.getId());
                int size = actionDetailParam.getTargetParamList().size();
                targetParam.setOrder(++size);
                actionDetailParam.getTargetParamList().add(targetParam);
                fight.getActionParam().getActionDetailParams().add(actionDetailParam);
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324418);
                skillRecord.setT((long)(int)effector.getId());
            }
        }
        return null;
    }
}
