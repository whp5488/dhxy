package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import java.util.Arrays;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31419 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31419() {
    }
    
    @Override
    public void phyAttackEnd(Fight fight, FightObject effector, Object... args) {
        if (args != null && args.length > 1) {
            BattleUnit battleUnit = (BattleUnit)args[0];
            ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
            if (battleUnit != null && actionDetailParam != null) {
                FightObject target = battleUnit.getTarget();
                if (target == null || !target.isFallDown()) {
                    return;
                }
                NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 30.0, 20.0);
                if (map == null) {
                    return;
                }
                double percent = map.getDouble("percent");
                if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                    FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(1), Arrays.asList(new String[0]));
                    int addLife = (int)((double)unit.getMaxLife() * 0.6);
                    if (addLife > 0) {
                        FightObjectParam unitParam = actionDetailParam.gainTargetParam((int)unit.getId(), true);
                        PassiveSkillUtils.excuteChangeLife(fight, unit, unitParam, addLife);
                        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31419);
                        skillRecord.setT((long)(int)unit.getId());
                    }
                }
            }
        }
    }
}
