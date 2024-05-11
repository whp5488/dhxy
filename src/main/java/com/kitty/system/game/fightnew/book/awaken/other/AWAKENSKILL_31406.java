
package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31406 extends AwakenSkillProcessor
{
    @Override
    public void magicAttackEndOther(final Fight fight, final FightObject effector, final Object... args) {
        if (effector.isPet()) {
            final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 70.0, 20.0);
            if (map != null) {
                final double percent = map.getDouble("percent");
                if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                    final FightObject target = PassiveSkillUtils.getRandomTarget(fight, effector, (byte)0);
                    if (target == null) {
                        return;
                    }
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31406);
                    skillRecord.setT(target.getId());
                    int level = effector.getLevel();
                    if (level <= 0) {
                        level = 1;
                    }
                    final double hurt = BigDecimalUtil.decimalScaleObjectDown(0, 80 * level + effector.getMaxMana() / 100 * 6 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose()));
                    final int harm = PetSkillEffectorUtil.wuXingFireCount(fight, effector, target, hurt);
                    ActionParamUtils.changeLife(fight, effector, target, 7, 31406, 2, -harm, false);
                }
            }
        }
    }
}
