package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.stream.Collectors;
import java.util.List;
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

public class FU_322501 extends TianFuProcessor
{
    public FU_322501() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (!target.isPlayer()) {
            return null;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322501);
        if (tianFuObject == null) {
            return null;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 322501);
        if (this.getTriggerPercent(fight, target.getId())) {
            Integer hurt = (Integer)args[0];
            if (hurt == null || (int)hurt <= 0) {
                return null;
            }
            double percenta = 4.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int alterHurt = (int)((double)(int)hurt * percenta);
            NutMap map = NutMap.NEW();
            map.setv("alterHurt", Integer.valueOf(-alterHurt));
            skillRecord.setT((long)(int)target.getId());
            return map;
        }
        else {
            return null;
        }
    }
    
    public boolean getTriggerPercent(Fight fight, Integer targetId) {
        double percent = 0.2;
        List<FightObject> mySide = (List)fight.getMySide((int)targetId).stream().filter(obj -> obj.isFallDown()).collect(Collectors.toList());
        if (mySide != null && mySide.size() > 0) {
            percent += percent * (double)mySide.size();
        }
        return SkillAi.canAffectOnPro(percent);
    }
}
