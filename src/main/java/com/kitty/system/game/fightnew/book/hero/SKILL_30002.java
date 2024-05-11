
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30002 extends SkillProcessor
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
        final RoleSkill skill = effector.getSkillBy(30002);
        if (skill != null) {
            return Math.min(5, BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.35) * 3.0 / 100.0)));
        }
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(30002);
        if (skill != null) {
            final Effect e = new Effect();
            int time = 0;
            if (!target.isFrozen()) {
                time = this.time(skill);
            }
            e.setTime(time);
            e.setType(1002);
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return BigDecimalUtil.decimalScaleIntegerDown(0, 75.0 * (Math.pow(skill.getProficient(), 0.77) * 5.0 / 100.0 + 1.0));
    }
    
    public int time(final RoleSkill skill) {
        int time = 3;
        if (skill != null) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(skill.getProficient(), 0.3) * 5.0 / 100.0));
        }
        return time;
    }
}
