package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323205 extends TianFuProcessor
{
    public FU_323205() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (!target.isPet() || target.getHostId() == (int)effector.getId() || fight.getRound() > 3 || fight.isPvp() != 1) {
            return null;
        }
        FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
        DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323205);
        if (tianFuObject == null) {
            return null;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 323205);
        double percent = (double)(5 + 4 * tianFuObject.getLevel()) * (1.0 + (double)dayInCeFight.getLevel() * 0.002) / 100.0;
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            FightObject unit = SkillAi.effectProBass(fight, target, Integer.valueOf(1), new ArrayList<>());
            int addLife = 8480;
            int oldLife = unit.getCurrLife();
            if (unit.getCurrLife() + addLife > unit.getMaxLife()) {
                addLife = unit.getMaxLife() - unit.getCurrLife();
            }
            boolean isRevive = false;
            if (oldLife <= 0) {
                isRevive = true;
            }
            ActionParamUtils.changeLife(fight, target, unit, 7, 300999, 2, addLife, isRevive);
            skillRecord.setT((long)(int)unit.getId());
        }
        return null;
    }
}
