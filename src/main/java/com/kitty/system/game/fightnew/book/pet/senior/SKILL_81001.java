
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81001 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        if (effector.getCurrLife() < effector.getMaxLife() * 0.4) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81001);
            final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 1.5 * (effector.getRelive() * 0.5 + 1.0) * (Math.pow(effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0 / (100 + effector.getRelive() * 20))) / 100.0);
            if (SkillAi.canAffectOnPro(percent)) {
                final int round = 3;
                effector.getStatusMap().setv(String.valueOf(1011), round);
                effector.getStatusMap().setv("1011value", 99);
                ActionParamUtils.addStatus(fight, effector, effector, 7, 81001, 2, 1011, round);
                skillRecord.setT(effector.getId());
            }
        }
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
