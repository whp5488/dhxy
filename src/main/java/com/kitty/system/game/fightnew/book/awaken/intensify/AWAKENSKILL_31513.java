package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31513 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31513() {
    }
    
    @Override
    public void phyAttackEnd(Fight fight, FightObject effector, Object... args) {
        if (args != null && args.length > 1) {
            BattleUnit battleUnit = (BattleUnit)args[0];
            ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
            if (battleUnit != null && actionDetailParam != null) {
                FightObject target = battleUnit.getTarget();
                if (target == null || target.isFallDown()) {
                    return;
                }
                NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 25.0, 20.0);
                if (map == null) {
                    return;
                }
                double percent = map.getDouble("percent");
                if (percent > 0.0) {
                    int currLife = target.getCurrLife();
                    int hurt = (int)((double)currLife * percent);
                    if (hurt > 0) {
                        hurt = ((hurt > 300000) ? 300000 : hurt);
                        FightObjectParam targetParam = ActionParamUtils.changeLifeForTarget(fight, target, actionDetailParam.getTargetParamList().size() + 1, -hurt, 1);
                        actionDetailParam.getTargetParamList().add(targetParam);
                        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31513);
                        skillRecord.setT((long)(int)target.getId());
                    }
                }
            }
        }
    }
}
