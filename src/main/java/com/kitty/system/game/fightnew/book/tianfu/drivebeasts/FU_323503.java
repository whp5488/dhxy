package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323503 extends TianFuProcessor
{
    public FU_323503() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (effector.isPet()) {
            Double percent = this.getPercent(fight, effector);
            if (percent == null) {
                return null;
            }
            int dist = PRO_DICT_TYPE.BURN_HURT.getDist();
            double value = ImbueUtil.getProValue(effector.getImbueMap(), Integer.valueOf(dist));
            if (value < 0.3) {
                value += (double)percent;
                if (value > 0.3) {
                    value = 0.3;
                }
                ImbueUtil.addOrUpdSkill(effector.getImbueMap(), Integer.valueOf(dist), WarMaxRound.WAR_MAX_ROUND.getValue(), 323503, value);
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 323503);
            skillRecord.setT((long)(int)effector.getId());
        }
        return null;
    }
    
    private Double getPercent(Fight fight, FightObject effector) {
        FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
        DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323503);
        if (tianFuObject == null) {
            return null;
        }
        double percent = 0.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        return Double.valueOf(percent);
    }
}
