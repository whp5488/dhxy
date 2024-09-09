package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31401 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31401() {
    }
    
    @Override
    public NutMap revive(Fight fight, FightObject effector, Object... args) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 5.0, 20.0);
        if (map == null) {
            return null;
        }
        double percent = map.getDouble("percent");
        boolean flag = false;
        if (percent > 0.0 && effector.isPet() && (effector.isFallDown() || effector.getDieRound() == fight.getRound()) && SkillAi.canAffectOnPro(percent)) {
            int life = effector.getMaxLife();
            List<Integer> clearStatusList = effector.getStatusList(StatusConst.getAnomalousStatus());
            if (clearStatusList != null && clearStatusList.size() > 0) {
                effector.clearStatus(clearStatusList);
                ActionParamUtils.changeLifeClearStatus(fight, effector, effector, 7, 31401, 2, life, true, clearStatusList);
            }
            else {
                ActionParamUtils.changeLife(fight, effector, effector, 7, 31401, 2, life, true);
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31401);
            skillRecord.setT((long)(int)effector.getId());
            flag = true;
        }
        return NutMap.NEW().setv("successful", Boolean.valueOf(flag));
    }
}
