
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83019 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (args.length > 0 && args[0] instanceof Integer) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83019);
            int hurt = (int)args[0];
            final double percenta = BigDecimalUtil.decimalScaleObjectDown(3, (16.0 + 5.2 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            final double percentb = BigDecimalUtil.decimalScaleObjectDown(3, (8.0 + 2.6 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            final double percentc = BigDecimalUtil.decimalScaleObjectDown(3, (4.0 + 1.3 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            final double percentd = BigDecimalUtil.decimalScaleObjectDown(3, (28.0 + 9.1 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            if (SkillAi.canAffectOnPro(percenta)) {
                hurt = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(hurt), String.valueOf(1.5)));
            }
            else if (SkillAi.canAffectOnPro(percentb)) {
                hurt = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(hurt), String.valueOf(2)));
            }
            else if (SkillAi.canAffectOnPro(percentc)) {
                hurt = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(hurt), String.valueOf(2.5)));
            }
            else if (SkillAi.canAffectOnPro(percentd)) {
                hurt = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(hurt), String.valueOf(0.5)));
            }
            skillRecord.setT(target.getId());
            return new Effect(0, hurt);
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
