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
    public AWAKENSKILL_31406() {
    }
    
    @Override
    public void magicAttackEndOther(Fight fight, FightObject effector, Object... args) {
        if (effector.isPet()) {
            NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 70.0, 20.0);
            if (map != null) {
                double percent = map.getDouble("percent");
                if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                    FightObject target = PassiveSkillUtils.getRandomTarget(fight, effector, (byte)0);
                    if (target == null) {
                        return;
                    }
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31406);
                    skillRecord.setT((long)(int)target.getId());
                    int level = effector.getLevel();
                    if (level <= 0) {
                        level = 1;
                    }
                    double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((double)(80 * level) + (double)(effector.getMaxMana() / 100 * 6) * ((double)effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
                    int harm = PetSkillEffectorUtil.wuXingFireCount(fight, effector, target, hurt);
                    ActionParamUtils.changeLife(fight, effector, target, 7, 31406, 2, -harm, false);
                }
            }
        }
    }
}
