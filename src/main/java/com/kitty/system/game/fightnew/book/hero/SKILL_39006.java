
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_39006 extends SkillProcessor
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
        List<FightObject> targetSide = fight.getTargetSide(effector.getId());
        if (targetSide.size() > 0) {
            targetSide = targetSide.stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
            if (targetSide.size() > 0) {
                return targetSide.size();
            }
        }
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final int currLife = target.getCurrLife();
        final Effect e = new Effect();
        e.setType(100000);
        e.setHurt(currLife);
        return e;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
