
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
    @Override
    public NutMap revive(final Fight fight, final FightObject effector, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 5.0, 20.0);
        if (map == null) {
            return null;
        }
        final double percent = map.getDouble("percent");
        boolean flag = false;
        if (percent > 0.0 && effector.isPet() && (effector.isFallDown() || effector.getDieRound() == fight.getRound()) && SkillAi.canAffectOnPro(percent)) {
            final int life = effector.getMaxLife();
            final List<Integer> clearStatusList = effector.getStatusList(StatusConst.getAnomalousStatus());
            if (clearStatusList != null && clearStatusList.size() > 0) {
                effector.clearStatus(clearStatusList);
                ActionParamUtils.changeLifeClearStatus(fight, effector, effector, 7, 31401, 2, life, true, clearStatusList);
            }
            else {
                ActionParamUtils.changeLife(fight, effector, effector, 7, 31401, 2, life, true);
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31401);
            skillRecord.setT(effector.getId());
            flag = true;
        }
        return NutMap.NEW().setv("successful", (Object)flag);
    }
}
