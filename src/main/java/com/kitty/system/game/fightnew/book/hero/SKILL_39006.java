package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_39006 extends SkillProcessor
{
    public SKILL_39006() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        List<FightObject> targetSide = fight.getTargetSide((int)effector.getId());
        if (targetSide.size() > 0) {
            targetSide = (List)targetSide.stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
            if (targetSide.size() > 0) {
                return targetSide.size();
            }
        }
        return 0;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        int currLife = target.getCurrLife();
        Effect e = new Effect();
        e.setType(100000);
        e.setHurt(currLife);
        return e;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
